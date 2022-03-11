import axios from "axios";
import React, { useCallback, useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { LoginContext } from "../../../contexts/login-context";
import Constant from "../../../shared/config/constant";
import BannerImage from "../../../shared/image/SVG/user_dashboard.svg";
import NotFound from "../../../shared/image/SVG/not-found.svg";
import "./user-dashboard.scss";

const UserDashboard = () => {
  const { authInfoContext } = useContext(LoginContext);
  const [authInfo, setAuthInfo] = authInfoContext;
  const [batchList, setBatchList] = useState([]);
  let navigate = useNavigate();

  const logout = useCallback(() => {
    alert("Session Expired");
    localStorage.clear();
    setAuthInfo(null);
    navigate("/");
  }, [setAuthInfo, navigate]);

  useEffect(() => {
    axios({
      method: "POST",
      url: Constant.BACKEND_BASE_URL + "/batch/batches",
      headers: {
        Authorization: `Bearer ${authInfo.token}`,
      },
      data: {
        userid: authInfo.userId,
      },
    })
      .then((response) => {
        setBatchList(response.data);
        if (!response.data) toast.error("No Batch Found");
      })
      .catch((error) => {
        if (!error.response) toast.error(error);
        else if (error.response.status === 403) logout();
      });
  }, [authInfo, logout]);

  const goToBatchDashboard = (batchId) => {
    navigate("/batch-dashboard/" + batchId);
  };

  return (
    <div className="user-dashboard">
      <section className="top-section">
        <div className="container">
          <div className="grid-wrapper">
            <div className="left-div">
              <p className="top-line">Hello {authInfo.fullName}</p>
              <h3>Welcome to User Dashboard</h3>
              <p className="desc">
                This is the user dashboard of BJIT Academy. From here you can access all of your enrolled batches and follow up all your training
                activities. You can also manage your personal information from user dashboard. Feel free to contact admin panel if you face any
                problem.
              </p>
            </div>
            <div className="right-div">
              <img src={BannerImage} alt="" />
            </div>
          </div>
        </div>
      </section>

      <section className="enrolled-batch container">
        <h3>My Enrolled Training Batches</h3>
        <hr />

        {batchList && (
          <div className="card-wrapper">
            {batchList.map((batch) => (
              <div className="card" key={batch.batchId}>
                <img src={batch.imageUrl} alt="" />
                <div className="text-wrapper">
                  <p className="title">{batch.batchName}</p>
                  <p className="description">{batch.description}</p>
                  <div className="button-wrapper">
                    <button className="button-outline" type="button" onClick={() => goToBatchDashboard(batch.batchId)}>
                      Batch Dashboard
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        {batchList.length === 0 && (
          <div className="not-found">
            <img src={NotFound} alt="" />
            <h3>Not Enrolled in Any Course</h3>
          </div>
        )}
      </section>
    </div>
  );
};

export default UserDashboard;
