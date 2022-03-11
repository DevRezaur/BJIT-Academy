import axios from "axios";
import React, { useCallback, useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { LoginContext } from "../../../contexts/login-context";
import Constant from "../../../shared/config/constant";
import "./list-batch.scss";

const ListBatch = () => {
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
      method: "GET",
      url: Constant.BACKEND_BASE_URL + "/batch/all",
      headers: {
        Authorization: `Bearer ${authInfo.token}`,
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

  const editBatch = (batchId) => {
    navigate("/admin-edit-batch/" + batchId);
  };

  const goToBatchDashboard = (batchId) => {
    navigate("/batch-dashboard/" + batchId);
  };

  return (
    <>
      <div className="list-batch container">
        <h3>All the Batches of BJIT Academy</h3>
        <hr />

        <div className="card-wrapper">
          {batchList.map((batch) => (
            <div className="card" key={batch.batchId}>
              <img src={batch.imageUrl} alt="" />
              <div className="text-wrapper">
                <p className="title">{batch.batchName}</p>
                <p className="description">{batch.description}</p>
                <div className="button-wrapper">
                  {authInfo && authInfo.roles[0] === "ROLE_ADMIN" && (
                    <button className="button-outline" type="button" onClick={() => editBatch(batch.batchId)}>
                      Edit Batch
                    </button>
                  )}
                  <button className="button-outline" type="button" onClick={() => goToBatchDashboard(batch.batchId)}>
                    Batch Dashboard
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
};

export default ListBatch;
