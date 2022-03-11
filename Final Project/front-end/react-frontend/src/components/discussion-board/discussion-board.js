import axios from "axios";
import React, { useCallback, useContext, useEffect, useRef, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import { LoginContext } from "../../contexts/login-context";
import Constant from "../../shared/config/constant";
import "./discussion-board.scss";

const DiscussionBoard = () => {
  const [messages, setMessages] = useState("");
  const [newMessage, setNewMessage] = useState("");
  const { authInfoContext } = useContext(LoginContext);
  const [authInfo, setAuthInfo] = authInfoContext;
  const { batchId } = useParams();
  let navigate = useNavigate();
  const el1 = useRef();
  const el2 = useRef();

  const logout = useCallback(() => {
    alert("Session Expired");
    localStorage.clear();
    setAuthInfo(null);
    navigate("/");
  }, [setAuthInfo, navigate]);

  const getAllMessages = useCallback(
    (batchId) => {
      axios({
        method: "GET",
        url: Constant.BACKEND_BASE_URL + "/message/" + batchId,
        headers: {
          Authorization: `Bearer ${authInfo.token}`,
        },
      })
        .then((response) => {
          setMessages(response.data);
        })
        .catch((error) => {
          if (error.response.status === 403) logout();
        });
    },
    [authInfo, logout]
  );

  useEffect(() => {
    let interval = setInterval(() => {
      getAllMessages(batchId);
    }, 3000);

    return () => clearInterval(interval);
  }, [batchId, getAllMessages]);

  useEffect(() => {
    el1.current.scrollIntoView({ block: "end" });
  }, [messages]);

  useEffect(() => {
    el2.current.scrollIntoView({ block: "end" });
  });

  const handleClick = () => {
    axios({
      method: "POST",
      url: Constant.BACKEND_BASE_URL + "/message",
      headers: {
        Authorization: `Bearer ${authInfo.token}`,
      },
      data: {
        batchId: batchId,
        userId: authInfo.userId,
        fullName: authInfo.fullName,
        message: newMessage,
      },
    })
      .then((response) => {
        getAllMessages(batchId);
      })
      .catch((error) => {
        if (error.response.status === 403) logout();
        else toast.error("Failed to Send Message");
      });

    setNewMessage("");
    document.getElementById("send-message-box").value = "";
  };

  return (
    <>
      <div className="discussion-board">
        <div className="all-messages">
          {messages &&
            messages.map((message) => (
              <div className="single-message" key={message.messageId}>
                <p className="name">{message.fullName}</p>
                <p className="text">{message.message}</p>
              </div>
            ))}
          <div id="el1" ref={el1}></div>
        </div>

        <div className="send-message">
          <textarea type="text" id="send-message-box" onChange={(e) => setNewMessage(e.target.value)} />
          <button type="button" disabled={newMessage === ""} onClick={handleClick}>
            Send
          </button>
        </div>
      </div>
      <div id="el2" ref={el2}></div>
    </>
  );
};

export default DiscussionBoard;
