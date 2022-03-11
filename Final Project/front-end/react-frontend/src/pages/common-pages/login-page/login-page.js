import "./login-page.scss";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import Constant from "../../../shared/config/constant";
import axios from "axios";
import { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { LoginContext } from "../../../contexts/login-context";

const LoginPage = () => {
  const [warning, setWarning] = useState("");
  const { authInfoContext } = useContext(LoginContext);
  const setAuthInfo = authInfoContext[1];
  let navigate = useNavigate();

  const schema = yup.object().shape({
    email: yup.string().email().required(),
    password: yup.string().min(3).max(32).required(),
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm({
    resolver: yupResolver(schema),
  });

  const onSubmitHandler = async (data) => {
    await axios({
      method: "POST",
      url: Constant.BACKEND_BASE_URL + "/auth/authenticate",
      data: {
        username: data.email,
        password: data.password,
      },
    })
      .then((response) => {
        localStorage.setItem("auth-info", JSON.stringify(response.data));
        setAuthInfo(response.data);
        if (response.data.roles[0] === "ROLE_ADMIN") navigate("/admin-dashboard");
        else if (response.data.roles[0] === "ROLE_USER") navigate("/user-dashboard");
      })
      .catch((error) => {
        setWarning(error.response && error.response.data?.message);
      });
    reset();
  };

  return (
    <>
      <section className="login-page">
        <form className="form-box" onSubmit={handleSubmit(onSubmitHandler)}>
          <h3>BJIT Academy Login</h3>
          <p className="warning">{warning}</p>

          <div className="input-box">
            <label>BJIT Email</label>
            <input {...register("email")} placeholder="Enter your email" type="email" />
            <p>{errors.email?.message}</p>
          </div>

          <div className="input-box">
            <label>Password</label>
            <input {...register("password")} placeholder="Enter your password" type="password" />
            <p>{errors.password?.message}</p>
          </div>

          <button type="submit" className="button-primary">
            Login
          </button>
        </form>
      </section>
    </>
  );
};

export default LoginPage;
