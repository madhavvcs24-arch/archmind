import { jsPDF } from "jspdf";

export function generateReport({
  analysis,
  quality,
  dependencies,
}) {
  const doc = new jsPDF();

  let y = 20;

  // -----------------------------------------
  // Title
  // -----------------------------------------

  doc.setFontSize(22);
  doc.text("ArchMind Architecture Report", 20, y);

  y += 12;

  doc.setFontSize(11);
  doc.text(
    "Analysis of the uploaded Java project",
    20,
    y
  );

  y += 15;

  // -----------------------------------------
  // Project Statistics
  // -----------------------------------------

  doc.setFontSize(16);
  doc.text("Project Statistics", 20, y);

  y += 10;

  doc.setFontSize(11);

  doc.text(
    `Packages: ${analysis?.packageCount ?? 0}`,
    20,
    y
  );

  y += 7;

  doc.text(
    `Classes: ${analysis?.classCount ?? 0}`,
    20,
    y
  );

  y += 7;

  doc.text(
    `Average Classes / Package: ${
      analysis?.averageClassesPerPackage?.toFixed?.(2) ?? "0.00"
    }`,
    20,
    y
  );

  y += 15;

  // -----------------------------------------
  // Architecture Score
  // -----------------------------------------

  doc.setFontSize(16);
  doc.text("Architecture Quality", 20, y);

  y += 10;

  doc.setFontSize(12);

  doc.text(
    `Architecture Score: ${
      quality?.architectureScore ?? 0
    } / 100`,
    20,
    y
  );

  y += 12;

  // -----------------------------------------
  // Coupling
  // -----------------------------------------

  doc.setFontSize(16);
  doc.text("Coupling Analysis", 20, y);

  y += 10;

  doc.setFontSize(10);

  const coupling =
    quality?.coupling ?? {};

  const couplingEntries =
    Object.entries(coupling);

  if (couplingEntries.length === 0) {

    doc.text(
      "No coupling information available.",
      20,
      y
    );

    y += 10;

  } else {

    couplingEntries.forEach(
      ([className, count]) => {

        if (y > 275) {

          doc.addPage();

          y = 20;

        }

        doc.text(
          `${className}: ${count} dependencies`,
          20,
          y
        );

        y += 6;

      }
    );
  }

  y += 8;

  // -----------------------------------------
  // Architecture Warnings
  // -----------------------------------------

  if (y > 250) {

    doc.addPage();

    y = 20;

  }

  doc.setFontSize(16);
  doc.text(
    "Architecture Warnings",
    20,
    y
  );

  y += 10;

  doc.setFontSize(10);

  const warnings =
    quality?.warnings ?? [];

  if (warnings.length === 0) {

    doc.text(
      "No architecture warnings.",
      20,
      y
    );

    y += 8;

  } else {

    warnings.forEach(
      (warning) => {

        const lines =
          doc.splitTextToSize(
            `• ${warning}`,
            170
          );

        if (y + lines.length * 5 > 275) {

          doc.addPage();

          y = 20;

        }

        doc.text(
          lines,
          20,
          y
        );

        y +=
          lines.length * 5 + 3;

      }
    );
  }

  y += 8;

  // -----------------------------------------
  // Dependency Summary
  // -----------------------------------------

  if (y > 250) {

    doc.addPage();

    y = 20;

  }

  doc.setFontSize(16);

  doc.text(
    "Dependency Analysis",
    20,
    y
  );

  y += 10;

  doc.setFontSize(10);

  const dependencyList =
    dependencies?.dependencies ?? [];

  doc.text(
    `Total dependencies: ${dependencyList.length}`,
    20,
    y
  );

  y += 10;

  dependencyList.forEach(
    (dependency) => {

      if (y > 275) {

        doc.addPage();

        y = 20;

      }

      doc.text(
        `${dependency.source} -> ${dependency.target} (${dependency.type})`,
        20,
        y
      );

      y += 6;

    }
  );

  // -----------------------------------------
  // Footer
  // -----------------------------------------

  const pageCount =
    doc.internal.getNumberOfPages();

  for (
    let page = 1;
    page <= pageCount;
    page++
  ) {

    doc.setPage(page);

    doc.setFontSize(8);

    doc.text(
      `ArchMind - Architecture Report | Page ${page} of ${pageCount}`,
      20,
      290
    );

  }

  // -----------------------------------------
  // Download
  // -----------------------------------------

  doc.save(
    "archmind-architecture-report.pdf"
  );
}