import { useEffect, useRef, useState } from "react";
import { useLocation, Navigate } from "react-router-dom";

import { generateReport } from "../utils/generateReport";

import {
  FaFolderOpen,
  FaCube,
  FaProjectDiagram,
  FaExclamationTriangle,
  FaCheckCircle,
  FaChartLine,
} from "react-icons/fa";

import { CircularProgressbar } from "react-circular-progressbar";

import mermaid from "mermaid";

import {
  TransformWrapper,
  TransformComponent,
} from "react-zoom-pan-pinch";

import DependencyGraph from "../components/graph/DependencyGraph";

import "react-circular-progressbar/dist/styles.css";

function ResultPage() {
  const { state } = useLocation();

  const diagramRef = useRef(null);

  const [diagramView, setDiagramView] = useState("class");

  // -----------------------------------------
  // Extract result data
  // -----------------------------------------

  const analysis = state?.analysis;

  const classDiagram = state?.diagram;

  const packageDiagram = state?.packageDiagram;

  const dependencies = state?.dependencies;

  const quality = state?.quality;

  // -----------------------------------------
  // Download Architecture Report
  // -----------------------------------------

  const handleDownloadReport = () => {
    try {
      generateReport({
        analysis,
        quality,
        dependencies,
      });
    } catch (error) {
      console.error(
        "Report generation failed:",
        error
      );

      alert(
        "Failed to generate the architecture report."
      );
    }
  };

  // -----------------------------------------
  // Current Mermaid diagram
  // -----------------------------------------

  const diagram =
    diagramView === "class"
      ? classDiagram
      : packageDiagram;

  // -----------------------------------------
  // Quality values
  // -----------------------------------------

  const architectureScore =
    Number(quality?.architectureScore ?? 0);

  const coupling =
    quality?.coupling ?? {};

  const warnings =
    quality?.warnings ?? [];
  const recommendations =
    quality?.recommendations ?? [];
  const scoreBreakdown =
    quality?.scoreBreakdown ?? [];

  // -----------------------------------------
  // Score label
  // -----------------------------------------

  const getScoreLabel = (score) => {
    if (score >= 90) {
      return "Excellent";
    }

    if (score >= 75) {
      return "Good";
    }

    if (score >= 50) {
      return "Needs Improvement";
    }

    return "Poor";
  };

  const scoreLabel =
    getScoreLabel(architectureScore);

  // -----------------------------------------
  // Render Mermaid diagram
  // -----------------------------------------

  useEffect(() => {
    if (
      !diagram?.diagram ||
      !diagramRef.current
    ) {
      return;
    }

    const renderDiagram = async () => {
      try {
        mermaid.initialize({
          startOnLoad: false,
          theme: "default",
          securityLevel: "loose",
        });

        const diagramId =
          `archmind-diagram-${Date.now()}`;

        const { svg } =
          await mermaid.render(
            diagramId,
            diagram.diagram
          );

        if (diagramRef.current) {
          diagramRef.current.innerHTML = svg;

          const renderedSvg =
            diagramRef.current.querySelector("svg");

          if (renderedSvg) {
            renderedSvg.style.width = "100%";
            renderedSvg.style.height = "auto";
            renderedSvg.style.maxWidth = "none";
          }
        }
      } catch (error) {
        console.error(
          "Mermaid rendering failed:",
          error
        );

        if (diagramRef.current) {
          diagramRef.current.innerHTML =
            "<p>Unable to render architecture diagram.</p>";
        }
      }
    };

    renderDiagram();
  }, [diagram]);

  // -----------------------------------------
  // Download Mermaid diagram
  // -----------------------------------------

  const downloadDiagram = () => {
    const svg =
      diagramRef.current?.querySelector("svg");

    if (!svg) {
      alert(
        "Diagram is not available yet. Please wait and try again."
      );

      return;
    }

    try {
      const svgClone =
        svg.cloneNode(true);

      svgClone.setAttribute(
        "xmlns",
        "http://www.w3.org/2000/svg"
      );

      svgClone.setAttribute(
        "xmlns:xlink",
        "http://www.w3.org/1999/xlink"
      );

      const viewBox =
        svg.getAttribute("viewBox");

      if (viewBox) {
        const values =
          viewBox.split(" ");

        if (values.length === 4) {
          svgClone.setAttribute(
            "width",
            values[2]
          );

          svgClone.setAttribute(
            "height",
            values[3]
          );
        }
      }

      const svgData =
        new XMLSerializer()
          .serializeToString(svgClone);

      const blob =
        new Blob(
          [svgData],
          {
            type:
              "image/svg+xml;charset=utf-8",
          }
        );

      const url =
        URL.createObjectURL(blob);

      const link =
        document.createElement("a");

      link.href = url;

      link.download =
        diagramView === "class"
          ? "archmind-class-diagram.svg"
          : "archmind-package-diagram.svg";

      document.body.appendChild(link);

      link.click();

      document.body.removeChild(link);

      setTimeout(() => {
        URL.revokeObjectURL(url);
      }, 1000);
    } catch (error) {
      console.error(
        "Diagram download failed:",
        error
      );

      alert(
        "Failed to download the architecture diagram."
      );
    }
  };

  // -----------------------------------------
  // Reset diagram
  // -----------------------------------------

  const resetDiagram = () => {
    document
      .querySelector(".diagram-reset-button")
      ?.click();
  };

  // -----------------------------------------
  // Redirect if no result state
  // -----------------------------------------

  if (!state) {
    return <Navigate to="/" />;
  }

  return (
    <div className="container py-5">

      {/* =====================================
          PAGE TITLE
      ====================================== */}

      <div className="mb-5">
        <h1 className="mb-2">
          Architecture Report
        </h1>

        <p className="text-muted mb-0">
          Analysis of the uploaded Java project
        </p>
      </div>


      {/* =====================================
          STATISTICS
      ====================================== */}

      <div className="row g-4">

        {/* Architecture Score */}

        <div className="col-lg-3 col-md-6">

          <div className="card shadow-sm p-4 text-center h-100">

            <h5 className="mb-3">
              Architecture Score
            </h5>

            <div
              style={{
                width: 140,
                height: 140,
                margin: "0 auto",
              }}
            >

              <CircularProgressbar
                value={architectureScore}
                text={`${architectureScore}`}
              />

            </div>

            <div className="mt-3">

              <span className="fw-semibold">
                {scoreLabel}
              </span>

              <div className="text-muted small">
                Overall architecture health
              </div>

            </div>

          </div>

        </div>


        {/* Packages */}

        <div className="col-lg-3 col-md-6">

          <div className="card shadow-sm p-4 text-center h-100">

            <FaFolderOpen
              size={35}
              className="mb-2"
            />

            <h6 className="mt-2">
              Packages
            </h6>

            <h3>
              {analysis?.packageCount ?? 0}
            </h3>

          </div>

        </div>


        {/* Classes */}

        <div className="col-lg-3 col-md-6">

          <div className="card shadow-sm p-4 text-center h-100">

            <FaCube
              size={35}
              className="mb-2"
            />

            <h6 className="mt-2">
              Classes
            </h6>

            <h3>
              {analysis?.classCount ?? 0}
            </h3>

          </div>

        </div>


        {/* Average */}

        <div className="col-lg-3 col-md-6">

          <div className="card shadow-sm p-4 text-center h-100">

            <FaProjectDiagram
              size={35}
              className="mb-2"
            />

            <h6 className="mt-2">
              Avg / Package
            </h6>

            <h3>
              {analysis?.averageClassesPerPackage
                ? analysis.averageClassesPerPackage.toFixed(2)
                : "0.00"}
            </h3>

          </div>

        </div>

      </div>


      {/* =====================================
          QUALITY SUMMARY
      ====================================== */}

      <div className="card shadow-sm mt-5 p-4">

        <div className="d-flex justify-content-between align-items-center mb-4">

          <div>

            <h3 className="mb-1">
              Architecture Quality
            </h3>

            <p className="text-muted mb-0">
              Structural health of the analyzed project.
            </p>

          </div>

          <FaChartLine size={32} />

        </div>


        <div className="row g-4">

          {/* Score */}

          <div className="col-md-4">

            <div
              className="border rounded p-4 text-center h-100"
            >

              <h6 className="text-muted">
                Architecture Score
              </h6>

              <h2 className="mt-2 mb-1">
                {architectureScore}/100
              </h2>

              <span className="text-muted">
                {scoreLabel}
              </span>

            </div>

          </div>


          {/* Coupling */}

          <div className="col-md-4">

            <div
              className="border rounded p-4 h-100"
            >

              <h6 className="text-muted text-center">
                Coupling
              </h6>

              {Object.keys(coupling).length === 0 ? (

                <div className="text-center mt-3 text-muted">
                  No coupling data available.
                </div>

              ) : (

                <div className="mt-3">

                  {Object.entries(coupling).map(
                    ([className, count]) => (

                      <div
                        key={className}
                        className="d-flex justify-content-between align-items-center border-bottom py-2"
                      >

                        <span className="fw-semibold">
                          {className}
                        </span>

                        <span className="badge bg-primary">
                          {count}
                        </span>

                      </div>

                    )
                  )}

                </div>

              )}

            </div>

          </div>


          {/* Warnings */}

          <div className="col-md-4">

            <div
              className="border rounded p-4 text-center h-100"
            >

              <h6 className="text-muted">
                Architecture Warnings
              </h6>

              <h2 className="mt-2 mb-1">
                {warnings.length}
              </h2>

              <span className="text-muted">
                Issues detected
              </span>

            </div>

          </div>

        </div>

      </div>
      {
      /* =====================================
          SCORE BREAKDOWN
      ====================================== */}

      <div className="card shadow-sm mt-4 p-4">

        <div className="d-flex align-items-center gap-2 mb-3">

          <FaChartLine size={25} />

          <h3 className="mb-0">
            Score Breakdown
          </h3>

        </div>

        {scoreBreakdown.length === 0 ? (

          <div className="text-muted">
            No score breakdown available.
          </div>

        ) : (

          <div className="d-flex flex-column gap-2">

            {scoreBreakdown.map(
              (item, index) => (

                <div
                  key={index}
                  className="border rounded p-3"
                >

                  {item}

                </div>

              )
            )}

          </div>

        )}

      </div>

      {/* =====================================
          ARCHITECTURE DIAGRAM
      ====================================== */}

      <div className="card shadow-sm mt-5 p-4">

        <div className="d-flex justify-content-between align-items-center">

          <h3 className="mb-0">
            Architecture Diagram
          </h3>


          <div className="d-flex gap-2">

            {/* Class View */}

            <button
              className={
                diagramView === "class"
                  ? "btn btn-primary"
                  : "btn btn-outline-primary"
              }
              onClick={() =>
                setDiagramView("class")
              }
            >
              Class View
            </button>


            {/* Package View */}

            <button
              className={
                diagramView === "package"
                  ? "btn btn-primary"
                  : "btn btn-outline-primary"
              }
              onClick={() =>
                setDiagramView("package")
              }
            >
              Package View
            </button>


            {/* Download Diagram */}

            <button
              className="btn btn-primary"
              onClick={downloadDiagram}
            >
              Download Diagram
            </button>


            {/* Download Report */}

            <button
              className="btn btn-success"
              onClick={handleDownloadReport}
            >
              Download Report
            </button>

          </div>

        </div>


        {/* Diagram controls */}

        <div className="mt-3 mb-2 d-flex gap-2">

          <button
            className="btn btn-outline-secondary btn-sm"
            onClick={resetDiagram}
          >
            Reset View
          </button>

        </div>


        {/* Mermaid diagram */}

        <div
          style={{
            width: "100%",
            height: "600px",
            overflow: "hidden",
            border: "1px solid #dee2e6",
            borderRadius: "8px",
            background: "#ffffff",
          }}
        >

          <TransformWrapper
            initialScale={1}
            minScale={0.4}
            maxScale={3}
            centerOnInit={true}
            wheel={{
              step: 0.1,
            }}
            doubleClick={{
              disabled: true,
            }}
            panning={{
              disabled: false,
            }}
          >

            {({ resetTransform }) => (

              <>

                <button
                  className="diagram-reset-button"
                  style={{
                    display: "none",
                  }}
                  onClick={() =>
                    resetTransform()
                  }
                />


                <TransformComponent
                  wrapperStyle={{
                    width: "100%",
                    height: "100%",
                  }}
                  contentStyle={{
                    width: "100%",
                    minHeight: "100%",
                    display: "flex",
                    justifyContent:
                      "center",
                    alignItems:
                      "center",
                  }}
                >

                  <div
                    ref={diagramRef}
                    style={{
                      textAlign: "center",
                    }}
                  />

                </TransformComponent>

              </>

            )}

          </TransformWrapper>

        </div>


        <small className="text-muted mt-2">
          Use the mouse wheel to zoom and drag the diagram to move it.
        </small>

      </div>


      {/* =====================================
          DEPENDENCY GRAPH
      ====================================== */}

      <div className="card shadow-sm mt-4 p-4">

        <div className="d-flex justify-content-between align-items-center mb-3">

          <div>

            <h3 className="mb-1">
              Dependency Graph
            </h3>

            <small className="text-muted">
              Shows dependencies between classes detected from imports.
            </small>

          </div>


          <div className="text-muted">

            {dependencies?.dependencies?.length ?? 0}
            {" "}
            dependencies

          </div>

        </div>


        <DependencyGraph
          dependencies={dependencies}
        />

      </div>
     {/* =====================================
          ARCHITECTURE WARNINGS
      ====================================== */}

      <div className="card shadow-sm mt-4 p-4">

        <div className="d-flex align-items-center gap-2 mb-3">

          {warnings.length === 0 ? (
            <FaCheckCircle size={25} />
          ) : (
            <FaExclamationTriangle size={25} />
          )}

          <h3 className="mb-0">
            Architecture Warnings
          </h3>

        </div>

        {warnings.length === 0 ? (

          <div className="border rounded p-3">

            <div className="d-flex align-items-center gap-2">

              <FaCheckCircle />

              <span>
                No architecture warnings detected.
              </span>

            </div>

          </div>

        ) : (

          <div className="d-flex flex-column gap-3">

            {warnings.map(
              (warning, index) => (

                <div
                  key={index}
                  className="border rounded p-3"
                >

                  <div className="d-flex align-items-start gap-3">

                    <FaExclamationTriangle
                      className="mt-1"
                      size={20}
                    />

                    <div>

                      <div className="fw-semibold">
                        Architecture Warning
                      </div>

                      <div className="text-muted mt-1">
                        {warning}
                      </div>

                    </div>

                  </div>

                </div>

              )
            )}

          </div>

        )}

      </div>


      {
        /* =====================================
          ARCHITECTURE RECOMMENDATIONS
      ====================================== */}

      <div className="card shadow-sm mt-4 p-4">

        <div className="d-flex align-items-center gap-2 mb-3">

          <FaChartLine size={25} />

          <h3 className="mb-0">
            Architecture Recommendations
          </h3>

        </div>

        {recommendations.length === 0 ? (

          <div className="border rounded p-3">

            <div className="text-muted">
              No recommendations available.
            </div>

          </div>

        ) : (

          <div className="d-flex flex-column gap-3">

            {recommendations.map(
              (recommendation, index) => (

                <div
                  key={index}
                  className="border rounded p-3"
                >

                  <div className="d-flex align-items-start gap-3">

                    <FaChartLine
                      className="mt-1"
                      size={20}
                    />

                    <div>

                      <div className="fw-semibold">
                        Recommendation
                      </div>

                      <div className="text-muted mt-1">
                        {recommendation}
                      </div>

                    </div>

                  </div>

                </div>

              )
            )}

          </div>

        )}

      </div>

    </div>
  );
}

export default ResultPage;