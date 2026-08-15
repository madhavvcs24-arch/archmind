import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/api";

function UploadBox() {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleAnalyze = async () => {
    if (!file) {
      alert("Please choose a ZIP file.");
      return;
    }

    setLoading(true);

    try {
      console.log("========== ANALYSIS START ==========");
      console.log("Selected file:", file.name);

      // -----------------------------------------
      // Project analysis
      // -----------------------------------------
      const uploadData = new FormData();
      uploadData.append("file", file);

      console.log("Calling /upload...");

      const analysis = await api.post(
        "/upload",
        uploadData
      );

      console.log("✅ /upload SUCCESS");
      console.log(analysis.data);

      // -----------------------------------------
      // Class diagram
      // -----------------------------------------
      const diagramData = new FormData();
      diagramData.append("file", file);

      console.log("Calling /diagram...");

      const diagram = await api.post(
        "/diagram",
        diagramData
      );

      console.log("✅ /diagram SUCCESS");
      console.log(diagram.data);

      // -----------------------------------------
      // Package diagram
      // -----------------------------------------
      const packageDiagramData = new FormData();
      packageDiagramData.append("file", file);

      console.log("Calling /package-diagram...");

      const packageDiagram = await api.post(
        "/package-diagram",
        packageDiagramData
      );

      console.log("✅ /package-diagram SUCCESS");
      console.log(packageDiagram.data);

      // -----------------------------------------
      // Dependency graph
      // -----------------------------------------
      const dependencyData = new FormData();
      dependencyData.append("file", file);

      console.log("Calling /dependencies...");

      const dependencies = await api.post(
        "/dependencies",
        dependencyData
      );

      console.log("✅ /dependencies SUCCESS");
      console.log(dependencies.data);

      // -----------------------------------------
      // Quality analysis
      // -----------------------------------------
      const qualityData = new FormData();
      qualityData.append("file", file);

      console.log("Calling /quality...");

      const quality = await api.post(
        "/quality",
        qualityData
      );

      console.log("✅ /quality SUCCESS");
      console.log("Quality response:");
      console.log(quality.data);

      // Check the new quality fields
      console.log(
        "Architecture Score:",
        quality.data.architectureScore
      );

      console.log(
        "Score Breakdown:",
        quality.data.scoreBreakdown
      );

      console.log(
        "Warnings:",
        quality.data.warnings
      );

      console.log(
        "Recommendations:",
        quality.data.recommendations
      );

      // -----------------------------------------
      // Navigate to result page
      // -----------------------------------------
      console.log("Navigating to Result page...");

      navigate("/result", {
        state: {
          analysis: analysis.data,
          diagram: diagram.data,
          packageDiagram: packageDiagram.data,
          dependencies: dependencies.data,

          // Pass the COMPLETE quality response
          // including scoreBreakdown, warnings,
          // recommendations, coupling and score.
          quality: quality.data,
        },
      });

    } catch (err) {

      console.error("========== ERROR ==========");
      console.error(err);

      if (err.response) {
        console.log("Status:", err.response.status);
        console.log("URL:", err.config?.url);
        console.log("Response:", err.response.data);

      } else if (err.request) {
        console.log("No response received from server.");
        console.log(err.request);

      } else {
        console.log("Message:", err.message);
      }

      console.log("========== END ERROR ==========");

      alert("Analysis failed.");

    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card shadow p-4">

      <h3 className="mb-4">
        Upload Java Project
      </h3>

      <input
        type="file"
        accept=".zip"
        className="form-control mb-3"
        onChange={(e) => {
          setFile(e.target.files[0]);
        }}
      />

      <button
        className="btn btn-primary w-100"
        onClick={handleAnalyze}
        disabled={loading}
      >
        {loading
          ? "Analyzing..."
          : "Analyze Project"}
      </button>

    </div>
  );
}

export default UploadBox;