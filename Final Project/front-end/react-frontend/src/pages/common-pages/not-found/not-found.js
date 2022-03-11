import React from "react";
import "./not-found.scss";
import NotFoundImage from "../../../shared/image/SVG/not-found.svg";
import { Link } from "react-router-dom";

const NotFound = () => {
  return (
    <div className="not-found-page">
      <img src={NotFoundImage} alt="" />
      <p>Resource Not Found</p>
      <button type="button" className="button-primary">
        <Link to={"./"}>Go Back</Link>
      </button>
    </div>
  );
};

export default NotFound;
