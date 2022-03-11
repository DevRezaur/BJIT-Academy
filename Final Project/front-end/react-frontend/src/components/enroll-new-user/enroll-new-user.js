import React, { useCallback, useContext } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { LoginContext } from "../../contexts/login-context";
import Constant from "../../shared/config/constant";
import { toast } from "react-toastify";
import "./enroll-new-user.scss";
import axios from "axios";

const EnrollNewUser = () => {
  const { authInfoContext } = useContext(LoginContext);
  const [authInfo, setAuthInfo] = authInfoContext;
  const { batchId } = useParams();
  let navigate = useNavigate();

  const logout = useCallback(() => {
    alert("Session Expired");
    localStorage.clear();
    setAuthInfo(null);
    navigate("/");
  }, [setAuthInfo, navigate]);

  const enrollUser = (userId) => {
    axios({
      method: "POST",
      url: Constant.BACKEND_BASE_URL + "/batch/enroll",
      headers: {
        Authorization: `Bearer ${authInfo.token}`,
      },
      data: {
        batchId: batchId,
        userId: userId,
      },
    })
      .then((response) => {
        toast.success("Successfully Enrolled User");
      })
      .catch((error) => {
        if (error.response.status === 403) logout();
        else toast.error("Failed to Enroll User");
      });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    let userId = event.target.userId.value;

    if (userId === "") toast.warning("User Id is Required");
    else {
      enrollUser(userId);
      event.target.userId.value = null;
    }
  };

  return (
    <>
      <form className="enroll-new-user-form" onSubmit={(e) => handleSubmit(e)}>
        <div className="info">
          <label>Enter Employee Id</label>
          <input type="number" name="userId" />
        </div>
        <button className="button-primary" type="submit">
          Add User
        </button>
      </form>
    </>
  );
};

export default EnrollNewUser;
