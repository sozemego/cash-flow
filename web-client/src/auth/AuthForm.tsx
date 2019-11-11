import React from "react";
import { Input } from "antd";
import Button from "antd/lib/button";
import {
  AUTH_SERVICE_LOGIN_URL,
  AUTH_SERVICE_SIGN_UP_URL
} from "../config/urls";
import { useDispatch } from "react-redux";
import { userLoggedIn } from "./actions";

export function AuthForm() {
  const dispatch = useDispatch();
  const [username, setUsername] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [error, setError] = React.useState("");

  async function login() {
    if (!username || !password) {
      setError("Username or password cannot be empty");
      return;
    }
    const response = await fetch(AUTH_SERVICE_LOGIN_URL, {
      method: "POST",
      body: JSON.stringify({ username, password }),
      headers: { "Content-Type": "application/json" }
    });
    const user = await response.json();
    dispatch(userLoggedIn(user));
  }

  async function signUp() {
    if (!username || !password) {
      setError("Username or password cannot be empty");
      return;
    }
    const response = await fetch(AUTH_SERVICE_SIGN_UP_URL, {
      method: "POST",
      body: JSON.stringify({ username, password }),
      headers: { "Content-Type": "application/json" }
    });
    const user = await response.json();
    dispatch(userLoggedIn(user));
  }

  return (
    <form
      style={{
        height: "100%",
        display: "flex",
        alignItems: "center",
        flexDirection: "column"
      }}
    >
      <div>Please log in or sign up for a new account!</div>
      <div>
        <Input
          placeholder="Username"
          value={username}
          onChange={e => {
            setError("");
            setUsername(e.target.value);
          }}
        />
      </div>
      <div>
        <Input
          placeholder="Password"
          value={password}
          onChange={e => {
            setError("");
            setPassword(e.target.value);
          }}
        />
      </div>
      <div>
        <Button type={"primary"} style={{ marginRight: "4px" }} onClick={login}>
          LOG IN
        </Button>
        <Button type={"primary"} onClick={signUp}>
          SIGN UP
        </Button>
      </div>
      <div style={{ color: "red" }}>{error}</div>
    </form>
  );
}
