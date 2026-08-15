import { useMemo, useRef, useState } from "react";

function DependencyGraph({ dependencies }) {
  const svgRef = useRef(null);

  const dependencyList = dependencies?.dependencies ?? [];

  const [layoutVersion, setLayoutVersion] = useState(0);

  // --------------------------------------------------
  // Collect unique nodes
  // --------------------------------------------------

  const nodes = useMemo(() => {
    const nodeSet = new Set();

    dependencyList.forEach((dependency) => {
      if (dependency.source) {
        nodeSet.add(dependency.source);
      }

      if (dependency.target) {
        nodeSet.add(dependency.target);
      }
    });

    return Array.from(nodeSet);
  }, [dependencyList]);

  // --------------------------------------------------
  // Calculate coupling
  //
  // Outgoing dependency count:
  //
  // A -> B
  // A -> C
  //
  // A has coupling = 2
  // --------------------------------------------------

  const coupling = useMemo(() => {
    const result = {};

    nodes.forEach((node) => {
      result[node] = 0;
    });

    dependencyList.forEach((dependency) => {
      if (dependency.source) {
        result[dependency.source] =
          (result[dependency.source] || 0) + 1;
      }
    });

    return result;
  }, [nodes, dependencyList]);

  // --------------------------------------------------
  // Find highest coupling
  // --------------------------------------------------

  const maxCoupling = useMemo(() => {
    return Math.max(
      0,
      ...Object.values(coupling)
    );
  }, [coupling]);

  // --------------------------------------------------
  // Determine severity
  // --------------------------------------------------

  const getSeverity = (node) => {
    const count = coupling[node] || 0;

    if (count >= 8) {
      return "critical";
    }

    if (count >= 5) {
      return "high";
    }

    return "normal";
  };

  // --------------------------------------------------
  // Colors
  // --------------------------------------------------

  const getNodeStyle = (node) => {
    const severity = getSeverity(node);

    if (severity === "critical") {
      return {
        fill: "#fff5f5",
        stroke: "#dc3545",
      };
    }

    if (severity === "high") {
      return {
        fill: "#fff8e1",
        stroke: "#fd7e14",
      };
    }

    return {
      fill: "#f8f9fa",
      stroke: "#0d6efd",
    };
  };

  // --------------------------------------------------
  // Empty state
  // --------------------------------------------------

  if (dependencyList.length === 0) {
    return (
      <div
        className="d-flex justify-content-center align-items-center"
        style={{
          height: "500px",
          border: "1px solid #dee2e6",
          borderRadius: "8px",
          background: "#ffffff",
        }}
      >
        <div className="text-center text-muted">
          <h5>No Dependencies Found</h5>

          <p className="mb-0">
            The analyzed project contains no detected dependencies.
          </p>
        </div>
      </div>
    );
  }

  // --------------------------------------------------
  // Graph dimensions
  // --------------------------------------------------

  const nodeCount = nodes.length;

  const columns = Math.max(
    4,
    Math.ceil(Math.sqrt(nodeCount))
  );

  const rows = Math.ceil(
    nodeCount / columns
  );

  const horizontalSpacing = 180;
  const verticalSpacing = 120;

  const padding = 100;

  const width = Math.max(
    1100,
    padding * 2 +
      (columns - 1) *
        horizontalSpacing
  );

  const height = Math.max(
    650,
    padding * 2 +
      (rows - 1) *
        verticalSpacing
  );

  // --------------------------------------------------
  // Calculate positions
  // --------------------------------------------------

  const positions = useMemo(() => {
    const result = {};

    nodes.forEach((node, index) => {
      const row = Math.floor(
        index / columns
      );

      const column =
        index % columns;

      result[node] = {
        x:
          padding +
          column *
            horizontalSpacing,

        y:
          padding +
          row *
            verticalSpacing,
      };
    });

    return result;
  }, [
    nodes,
    columns,
    padding,
    horizontalSpacing,
    verticalSpacing,
    layoutVersion,
  ]);

  // --------------------------------------------------
  // Download SVG
  // --------------------------------------------------

  const downloadSVG = () => {
    if (!svgRef.current) {
      return;
    }

    const svg =
      svgRef.current.cloneNode(true);

    svg.setAttribute(
      "xmlns",
      "http://www.w3.org/2000/svg"
    );

    const serializer =
      new XMLSerializer();

    const source =
      serializer.serializeToString(svg);

    const blob = new Blob(
      [source],
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
      "archmind-dependency-graph.svg";

    document.body.appendChild(link);

    link.click();

    document.body.removeChild(link);

    URL.revokeObjectURL(url);
  };

  // --------------------------------------------------
  // Download PNG
  // --------------------------------------------------

  const downloadPNG = () => {
    if (!svgRef.current) {
      return;
    }

    const svg =
      svgRef.current.cloneNode(true);

    svg.setAttribute(
      "xmlns",
      "http://www.w3.org/2000/svg"
    );

    const serializer =
      new XMLSerializer();

    const source =
      serializer.serializeToString(svg);

    const svgBlob = new Blob(
      [source],
      {
        type:
          "image/svg+xml;charset=utf-8",
      }
    );

    const url =
      URL.createObjectURL(svgBlob);

    const image = new Image();

    image.onload = () => {
      const scale = 2;

      const canvas =
        document.createElement(
          "canvas"
        );

      canvas.width =
        width * scale;

      canvas.height =
        height * scale;

      const context =
        canvas.getContext("2d");

      context.fillStyle =
        "#ffffff";

      context.fillRect(
        0,
        0,
        canvas.width,
        canvas.height
      );

      context.scale(
        scale,
        scale
      );

      context.drawImage(
        image,
        0,
        0,
        width,
        height
      );

      URL.revokeObjectURL(url);

      canvas.toBlob(
        (blob) => {
          if (!blob) {
            return;
          }

          const pngUrl =
            URL.createObjectURL(
              blob
            );

          const link =
            document.createElement(
              "a"
            );

          link.href = pngUrl;

          link.download =
            "archmind-dependency-graph.png";

          document.body.appendChild(
            link
          );

          link.click();

          document.body.removeChild(
            link
          );

          URL.revokeObjectURL(
            pngUrl
          );
        },
        "image/png"
      );
    };

    image.src = url;
  };

  // --------------------------------------------------
  // Reset layout
  // --------------------------------------------------

  const resetLayout = () => {
    setLayoutVersion(
      (value) => value + 1
    );
  };

  // --------------------------------------------------
  // Short class name
  // --------------------------------------------------

  const getShortName = (name) => {
    if (!name) {
      return "";
    }

    const parts =
      name.split(".");

    return parts[
      parts.length - 1
    ];
  };

  // --------------------------------------------------
  // Render
  // --------------------------------------------------

  return (
    <div
      style={{
        width: "100%",
        border:
          "1px solid #dee2e6",
        borderRadius: "8px",
        background: "#ffffff",
      }}
    >
      {/* ------------------------------------------
          Header
      ------------------------------------------- */}

      <div
        className="d-flex justify-content-between align-items-center flex-wrap gap-2"
        style={{
          padding: "12px 16px",
          borderBottom:
            "1px solid #dee2e6",
          background: "#f8f9fa",
        }}
      >
        <div>
          <h5
            style={{
              margin: 0,
              fontWeight: 600,
            }}
          >
            Dependency Graph
          </h5>

          <small className="text-muted">
            {nodeCount} classes ·{" "}
            {dependencyList.length}{" "}
            dependencies
          </small>
        </div>

        <div className="d-flex gap-2">
          <button
            type="button"
            className="btn btn-sm btn-outline-secondary"
            onClick={resetLayout}
          >
            Reset Layout
          </button>

          <button
            type="button"
            className="btn btn-sm btn-outline-primary"
            onClick={downloadSVG}
          >
            Download SVG
          </button>

          <button
            type="button"
            className="btn btn-sm btn-primary"
            onClick={downloadPNG}
          >
            Download PNG
          </button>
        </div>
      </div>

      {/* ------------------------------------------
          Legend
      ------------------------------------------- */}

      <div
        className="d-flex align-items-center flex-wrap gap-3"
        style={{
          padding:
            "10px 16px",
          borderBottom:
            "1px solid #dee2e6",
          fontSize: "13px",
        }}
      >
        <strong>
          Coupling:
        </strong>

        <span
          style={{
            display: "flex",
            alignItems:
              "center",
            gap: "6px",
          }}
        >
          <span
            style={{
              width: "12px",
              height: "12px",
              borderRadius:
                "50%",
              border:
                "2px solid #0d6efd",
              background:
                "#f8f9fa",
            }}
          />

          Normal
        </span>

        <span
          style={{
            display: "flex",
            alignItems:
              "center",
            gap: "6px",
          }}
        >
          <span
            style={{
              width: "12px",
              height: "12px",
              borderRadius:
                "50%",
              border:
                "2px solid #fd7e14",
              background:
                "#fff8e1",
            }}
          />

          High ≥ 5
        </span>

        <span
          style={{
            display: "flex",
            alignItems:
              "center",
            gap: "6px",
          }}
        >
          <span
            style={{
              width: "12px",
              height: "12px",
              borderRadius:
                "50%",
              border:
                "2px solid #dc3545",
              background:
                "#fff5f5",
            }}
          />

          Critical ≥ 8
        </span>

        <span className="text-muted">
          Highest:{" "}
          {maxCoupling}
        </span>
      </div>

      {/* ------------------------------------------
          Graph
      ------------------------------------------- */}

      <div
        style={{
          width: "100%",
          height: "600px",
          overflow: "auto",
          background:
            "#ffffff",
        }}
      >
        <svg
          ref={svgRef}
          width={width}
          height={height}
          viewBox={`0 0 ${width} ${height}`}
          style={{
            display: "block",
            background:
              "#ffffff",
          }}
        >
          <defs>
            <marker
              id="dependency-arrow"
              markerWidth="10"
              markerHeight="10"
              refX="9"
              refY="3"
              orient="auto"
              markerUnits="strokeWidth"
            >
              <path
                d="M0,0 L0,6 L9,3 z"
                fill="#6c757d"
              />
            </marker>
          </defs>

          <rect
            x="0"
            y="0"
            width={width}
            height={height}
            fill="#ffffff"
          />

          {/* --------------------------------------
              Edges
          --------------------------------------- */}

          {dependencyList.map(
            (
              dependency,
              index
            ) => {
              const source =
                positions[
                  dependency
                    .source
                ];

              const target =
                positions[
                  dependency
                    .target
                ];

              if (
                !source ||
                !target
              ) {
                return null;
              }

              return (
                <g
                  key={`dependency-${index}`}
                >
                  <line
                    x1={source.x}
                    y1={source.y}
                    x2={target.x}
                    y2={target.y}
                    stroke="#6c757d"
                    strokeWidth="1.5"
                    opacity="0.65"
                    markerEnd="url(#dependency-arrow)"
                  />

                  {dependency.type && (
                    <text
                      x={
                        (source.x +
                          target.x) /
                        2
                      }
                      y={
                        (source.y +
                          target.y) /
                          2 -
                        5
                      }
                      textAnchor="middle"
                      fontSize="9"
                      fill="#6c757d"
                      pointerEvents="none"
                    >
                      {
                        dependency.type
                      }
                    </text>
                  )}
                </g>
              );
            }
          )}

          {/* --------------------------------------
              Nodes
          --------------------------------------- */}

          {nodes.map((node) => {
            const position =
              positions[node];

            const label =
              getShortName(
                node
              );

            const style =
              getNodeStyle(
                node
              );

            const count =
              coupling[node] ||
              0;

            return (
              <g
                key={node}
                transform={`translate(${position.x}, ${position.y})`}
              >
                <circle
                  r="48"
                  fill={style.fill}
                  stroke={
                    style.stroke
                  }
                  strokeWidth="3"
                />

                <text
                  textAnchor="middle"
                  dominantBaseline="middle"
                  fontSize="12"
                  fontWeight="600"
                  fill="#212529"
                >
                  {label.length >
                  16
                    ? `${label.substring(
                        0,
                        14
                      )}...`
                    : label}
                </text>

                {/* Coupling count */}

                {count > 0 && (
                  <text
                    textAnchor="middle"
                    y="63"
                    fontSize="10"
                    fontWeight="600"
                    fill={
                      style.stroke
                    }
                  >
                    {count}{" "}
                    dependenc
                    {count === 1
                      ? "y"
                      : "ies"}
                  </text>
                )}

                <title>
                  {node}
                  {"\n"}
                  Outgoing
                  dependencies:{" "}
                  {count}
                </title>
              </g>
            );
          })}
        </svg>
      </div>

      {/* ------------------------------------------
          Footer
      ------------------------------------------- */}

      <div
        style={{
          padding:
            "8px 16px",
          borderTop:
            "1px solid #dee2e6",
          background:
            "#f8f9fa",
          fontSize: "12px",
          color: "#6c757d",
        }}
      >
        Hover over a node to see its full class name
        and coupling count.
      </div>
    </div>
  );
}

export default DependencyGraph;