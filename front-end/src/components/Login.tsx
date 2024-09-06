import React, { useState } from "react";

const Login = () => {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [showPassword, setShowPassword] = useState<boolean>(false);

  return (
    <div className="login-container container col-12 col-sm-4 d-flex flex-column">
      <div className="title text-center ">Log in</div>
      <div className="text">Email or Username</div>
      <input
        type="text"
        placeholder="Email or username"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <div className="password">
        <input
          type={showPassword === true ? "text" : "password"}
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <i
          className={
            showPassword === false
              ? "eyes fa-solid fa-eye-slash"
              : "eyes fa-solid fa-eye"
          }
          onClick={() => setShowPassword(!showPassword)}
        ></i>
      </div>
      <button
        disabled={email && password ? false : true}
        className={email && password ? "active" : ""}
      >
        Login
      </button>
      <div className="back">
        <i className="fa-solid fa-chevron-left"></i> Go back
      </div>
    </div>
  );
};

export default Login;
