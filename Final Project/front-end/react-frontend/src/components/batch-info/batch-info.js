import React, { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import { LoginContext } from "../../contexts/login-context";
import { toast } from "react-toastify";
import axios from "axios";
import Constant from "../../shared/config/constant";
import "./batch-info.scss";
import { useEffect } from "react/cjs/react.development";

const BatchInfo = ({ batchInfo }) => {
  const { authInfoContext } = useContext(LoginContext);
  const [authInfo, setAuthInfo] = authInfoContext;
  const [imageUrl, setImageUrl] = useState("https://devrezaur.com/File-Bucket/image/default_course.jpg");
  let navigate = useNavigate();

  const schema = yup.object().shape({
    batchName: yup.string().required(),
    description: yup.string().required(),
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(schema),
    defaultValues: batchInfo,
  });

  const logout = () => {
    alert("Session Expired");
    localStorage.clear();
    setAuthInfo(null);
    navigate("/");
  };

  useEffect(() => {
    if (batchInfo) setImageUrl(batchInfo.imageUrl);
  }, [batchInfo]);

  const uploadImage = async (event) => {
    const image = event.target.files[0];
    const formData = new FormData();
    formData.append("sendimage", image);

    await axios
      .post("https://devrezaur.com/File-Bucket/image-upload.php", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then((response) => {
        if (response.data) {
          let imageUrl = `https://devrezaur.com/File-Bucket/image/${event.target.files[0].name}`;
          setImageUrl(imageUrl);
        }
      })
      .catch((error) => {
        toast.error(error);
      });
  };

  const onSubmit = async (data) => {
    data = { ...data, imageUrl: imageUrl };

    await axios({
      method: "POST",
      url: Constant.BACKEND_BASE_URL + "/batch/add",
      headers: {
        Authorization: `Bearer ${authInfo.token}`,
      },
      data: data,
    })
      .then((response) => {
        if (response.data) {
          navigate("/admin-list-batch");
          toast.success("Batch Added Successfully!");
        }
      })
      .catch((error) => {
        if (!error.response) toast.error("Failed to Add Batch!");
        else if (error.response.status === 403) logout();
      });
  };

  return (
    <>
      <div className="batch-info">
        <form onSubmit={handleSubmit(onSubmit)}>
          <img src={imageUrl} alt="" />

          <div className="info">
            <label>Batch Name</label>
            <input type="text" {...register("batchName")} />
            <p className="warning">{errors.batchName?.message}</p>
          </div>

          <div className="info">
            <label>Description</label>
            <textarea type="text" {...register("description")} />
            <p className="warning">{errors.description?.message}</p>
          </div>

          <div className="info">
            <label>Banner Image</label>
            <input className="file-input" type="file" onChange={(e) => uploadImage(e)} />
          </div>

          <button className="button-primary" type="submit">
            Add Batch
          </button>
        </form>
      </div>
    </>
  );
};

export default BatchInfo;
