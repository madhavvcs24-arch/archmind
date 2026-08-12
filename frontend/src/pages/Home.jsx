import Navbar from "../components/layout/Navbar";
import UploadBox from "../components/upload/UploadBox";

function Home() {
  return (
    <>
      <Navbar />

      <div className="container mt-5">

        <h1 className="display-4">
          ArchMind
        </h1>

        <p className="text-muted mb-4">
          AI Powered Software Architecture Visualizer
        </p>

        <UploadBox />

      </div>
    </>
  );
}

export default Home;