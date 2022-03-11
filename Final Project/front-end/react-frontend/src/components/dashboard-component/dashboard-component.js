import axios from "axios";
import React, { useCallback, useContext, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import { LoginContext } from "../../contexts/login-context";
import Constant from "../../shared/config/constant";
import "./dashboard-component.scss";

const DashboardComponent = () => {
  const [posts, setPosts] = useState("");
  const [newPost, setNewPost] = useState("");
  const [file, setFile] = useState("");
  const [isLoading, setIsLoading] = useState(false);
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

  const getAllPosts = useCallback(
    (batchId) => {
      axios({
        method: "GET",
        url: Constant.BACKEND_BASE_URL + "/post/" + batchId,
        headers: {
          Authorization: `Bearer ${authInfo.token}`,
        },
      })
        .then((response) => {
          setPosts(response.data);
        })
        .catch((error) => {
          if (error.response.status === 403) logout();
          //else toast.error("No Post Found");
        });
    },
    [authInfo, logout]
  );

  useEffect(() => {
    getAllPosts(batchId);
  }, [batchId, getAllPosts]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!newPost) toast.error("Post Body Can't be Empty");
    else {
      axios({
        method: "POST",
        url: Constant.BACKEND_BASE_URL + "/post",
        headers: {
          Authorization: `Bearer ${authInfo.token}`,
        },
        data: {
          batchId: batchId,
          userId: authInfo.userId,
          description: newPost,
          resourceName: file,
          resourcesLink: "https://devrezaur.com/File-Bucket/file/" + file,
        },
      })
        .then((response) => {
          toast.success("Successfully Added New Post");
          getAllPosts(batchId);
          setNewPost("");
          setFile("");
          document.getElementById("file").value = "";
        })
        .catch((error) => {
          if (error.response.status === 403) logout();
          else toast.error("Failed to Post");
        });
    }
  };

  const uploadFile = async (event) => {
    const file = event.target.files[0];
    const formData = new FormData();
    formData.append("sendfile", file);
    setIsLoading(true);

    await axios({
      method: "POST",
      url: "https://devrezaur.com/File-Bucket/file-upload.php",
      headers: {
        "Content-Type": "multipart/form-data",
      },
      data: formData,
    })
      .then((response) => {
        if (response.data) {
          setFile(event.target.files[0].name);
          setIsLoading(false);
        } else {
          toast.error("Failed to upload file. Try again");
        }
      })
      .catch((error) => {
        toast.error(error);
        setIsLoading(false);
      });
  };
  return (
    <>
      <form className="new-post" onSubmit={handleSubmit}>
        <div className="info">
          <label>Post New Content</label>
          <textarea type="text" value={newPost} onChange={(e) => setNewPost(e.target.value)} />
        </div>

        {isLoading && <p className="warning">Uploading Content....</p>}
        <div className="button-wrapper">
          <input id="file" className="file-input" type="file" onChange={(e) => uploadFile(e)} />
          <button disabled={isLoading} className="button-primary" type="submit">
            Post
          </button>
        </div>
      </form>

      <hr />

      {posts &&
        posts.map((post) => (
          <div className="post" key={post.postId}>
            <div className="top-section">
              <img src={post.imageUrl} alt="" />
              <div>
                <p className="name">{post.fullName}</p>
                <p className="time">{post.createDate}</p>
              </div>
            </div>
            <p className="description">{post.desc}</p>
            <h4>Resource Material</h4>
            <a href={post.resourcesLink} download={post.resourceName}>
              {post.resourceName}
            </a>
          </div>
        ))}
    </>
  );
};

export default DashboardComponent;
