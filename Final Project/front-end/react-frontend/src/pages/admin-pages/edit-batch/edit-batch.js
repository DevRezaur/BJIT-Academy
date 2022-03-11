import React, { useContext } from "react";
import "./edit-batch.scss";
import BatchInfo from "../../../components/batch-info/batch-info";
import { useNavigate, useParams } from "react-router-dom";
import { useState } from "react/cjs/react.development";
import axios from "axios";
import Constant from "../../../shared/config/constant";
import { LoginContext } from "../../../contexts/login-context";
import { toast } from "react-toastify";

const EditBatch = () => {
  const { authInfoContext } = useContext(LoginContext);
  const [authInfo, setAuthInfo] = authInfoContext;
  const [batchInfo, setBatchInfo] = useState(null);
  const { batchId } = useParams();
  let navigate = useNavigate();

  const logout = () => {
    alert("Session Expired");
    localStorage.clear();
    setAuthInfo(null);
    navigate("/");
  };

  useState(() => {
    if (batchId) {
      axios({
        method: "GET",
        url: Constant.BACKEND_BASE_URL + "/batch/" + batchId,
        headers: {
          Authorization: `Bearer ${authInfo.token}`,
        },
      })
        .then((response) => {
          if (response.data) {
            setBatchInfo(response.data);
          }
        })
        .catch((error) => {
          if (!error.response) toast.error("Failed to Add Batch!");
          else if (error.response.status === 403) logout();
        });
    }
  }, []);

  return (
    <>
      <div className="edit-batch">
        <h3>Edit Training Batch Info</h3>

        {batchInfo && <BatchInfo batchInfo={batchInfo} />}
      </div>
    </>
  );
};

export default EditBatch;
