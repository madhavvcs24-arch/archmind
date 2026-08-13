import { useEffect, useRef, useState } from "react";
import { useLocation, Navigate } from "react-router-dom";
import { FaFolderOpen, FaCube, FaProjectDiagram } from "react-icons/fa";
import { CircularProgressbar } from "react-circular-progressbar";
import mermaid from "mermaid";
import {
  TransformWrapper,
  TransformComponent,
} from "react-zoom-pan-pinch";

import "react-circular-progressbar/dist/styles.css";

function ResultPage() {
  const { state } = useLocation();
  const diagramRef = useRef(null);
  const [diagramView, setDiagramView] = useState("class");

  const analysis = state?.analysis;

  const classDiagram = state?.diagram;
  const packageDiagram = state?.packageDiagram;
  const quality = state?.quality;

  const diagram =
    diagramView === "class"
      ? classDiagram
      : packageDiagram;

  // Render Mermaid diagram
  useEffect(() => {
    if (!diagram?.diagram || !diagramRef.current) {
      return;
    }

    const renderDiagram = async () => {
      try {
        mermaid.initialize({
          startOnLoad: false,
          theme: "default",
          securityLevel: "loose",
        });

        const diagramId = `archmind-diagram-${Date.now()}`;

        const { svg } = await mermaid.render(
          diagramId,
          diagram.diagram
        );

        if (diagramRef.current) {
          diagramRef.current.innerHTML = svg;
          const renderedSvg = diagramRef.current.querySelector("svg");

          if (renderedSvg) {
            renderedSvg.style.width = "100%";
            renderedSvg.style.height = "auto";
            renderedSvg.style.maxWidth = "none";
          }
        }
        
      } catch (error) {
        console.error("Mermaid rendering failed:", error);

        if (diagramRef.current) {
          diagramRef.current.innerHTML =
            "<p>Unable to render architecture diagram.</p>";
        }
      }
    };

    renderDiagram();
  }, [diagram]);

  // Download rendered diagram as PNG
  const downloadDiagram = () => {
    const svg = diagramRef.current?.querySelector("svg");

    if (!svg) {
      alert("Diagram is not available yet. Please wait and try again.");
      return;
    }

    try {
      // Clone the rendered SVG
      const svgClone = svg.cloneNode(true);

      // Make sure the SVG is standalone
      svgClone.setAttribute(
        "xmlns",
        "http://www.w3.org/2000/svg"
      );

      svgClone.setAttribute(
        "xmlns:xlink",
        "http://www.w3.org/1999/xlink"
      );

      // Get the diagram dimensions
      const viewBox = svg.getAttribute("viewBox");

      if (viewBox) {
        const values = viewBox.split(" ");

        if (values.length === 4) {
          svgClone.setAttribute("width", values[2]);
          svgClone.setAttribute("height", values[3]);
        }
      }

      // Convert SVG to text
      const svgData = new XMLSerializer().serializeToString(
        svgClone
      );

      // Create downloadable file
      const blob = new Blob(
        [svgData],
        {
          type: "image/svg+xml;charset=utf-8",
        }
      );

      const url = URL.createObjectURL(blob);

      // Create download link
      const link = document.createElement("a");

      link.href = url;
      link.download = "archmind-architecture-diagram.svg";

      document.body.appendChild(link);

      link.click();

      document.body.removeChild(link);

      // Clean up
      setTimeout(() => {
        URL.revokeObjectURL(url);
      }, 1000);

    } catch (error) {
      console.error("Diagram download failed:", error);
      alert("Failed to download the architecture diagram.");
    }
  };

  // Redirect if no result state exists
  if (!state) {
    return <Navigate to="/" />;
  }

  return (
    <div className="container py-5">

      <h1 className="mb-5">
        Architecture Report
      </h1>


      {/* Statistics */}
      <div className="row g-4">

        {/* Score */}
        <div className="col-lg-3">
          <div className="card shadow-sm p-4 text-center h-100">

            <h5 className="mb-3">
              Architecture Score
            </h5>

            <div
              style={{
                width: 140,
                margin: "auto",
              }}
            >
              <CircularProgressbar
                value={quality?.architectureScore ?? 0}
                text={`${quality?.architectureScore ?? 0}`}
              />
            </div>

          </div>
        </div>


        {/* Packages */}
        <div className="col-lg-3">
          <div className="card shadow-sm p-4 text-center h-100">

            <FaFolderOpen size={35} />

            <h6 className="mt-3">
              Packages
            </h6>

            <h3>
              {analysis?.packageCount ?? 0}
            </h3>

          </div>
        </div>


        {/* Classes */}
        <div className="col-lg-3">
          <div className="card shadow-sm p-4 text-center h-100">

            <FaCube size={35} />

            <h6 className="mt-3">
              Classes
            </h6>

            <h3>
              {analysis?.classCount ?? 0}
            </h3>

          </div>
        </div>


        {/* Average */}
        <div className="col-lg-3">
          <div className="card shadow-sm p-4 text-center h-100">

            <FaProjectDiagram size={35} />

            <h6 className="mt-3">
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


      {/* Architecture Diagram */}
      <div className="card shadow-sm mt-5 p-4">

        <div className="d-flex justify-content-between align-items-center">

          <h3 className="mb-0">
            Architecture Diagram
          </h3>

          <div className="d-flex gap-2">

            <button 
              className={
                diagramView === "class"
                  ? "btn btn-primary"
                  : "btn btn-outline-primary"
              }
              onClick={() => setDiagramView("class")}
            >
              Class View
            </button>

            <button
              className={
                diagramView === "package"
                  ? "btn btn-primary"
                  : "btn btn-outline-primary"
              }
              onClick={() => setDiagramView("package")}
            >
              Package View
            </button>

            <button
              className="btn btn-primary"
              onClick={downloadDiagram}
            >
              Download Diagram
            </button>

          </div>

        </div>

        {/* Diagram controls */}
        <div className="mt-3 mb-2 d-flex gap-2">

          <button
            className="btn btn-outline-secondary btn-sm"
            onClick={() => {
              document
                .querySelector(".diagram-reset-button")
                ?.click();
            }}
          >
            Reset View
          </button>

        </div>

        {/* Interactive Mermaid diagram */}
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
                  style={{ display: "none" }}
                  onClick={() => resetTransform()}
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
                    justifyContent: "center",
                    alignItems: "center",
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

      {/* Architecture Warnings */}
      <div className="card shadow-sm mt-4 p-4">

        <h3>
          Architecture Warnings
        </h3>

        <ul className="mt-3">

          {(quality?.warnings ?? []).map(
            (warning, index) => (
              <li key={index}>
                {warning}
              </li>
            )
          )}

          {(!quality?.warnings ||
            quality.warnings.length === 0) && (
            <li>
              No architecture warnings.
            </li>
          )}

        </ul>

      </div>

    </div>
  );
}

export default ResultPage;