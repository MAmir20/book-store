import React, { useState } from "react";
import { useHistory } from "react-router-dom";
import { Typography, Button, TextField, Grid } from "@material-ui/core";
import useStyles from './styles';
import { Link } from 'react-router-dom';
import signUp from "./SignUp";


const Login = (props) => { // Receive setIsLoggedIn as a prop
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const history = useHistory();
    const classes = useStyles();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`http://localhost:8087/api/users/checkCredentials?email=${email}&password=${password}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (response.ok) {
                const userId = await response.json();
                localStorage.setItem("userId", JSON.stringify(userId));
                props.setIsLoggedIn(true);
                props.handleLogin(userId);
                history.push("/");
            }
            else {
                setError("Password is incorrect or user is not defined.");
            }
        } catch (error) {
            console.error("Error authenticating user:", error);
            setError("An error occurred while authenticating. Please try again.");
        }
    };


    return (
        <Grid
            container
            justify="center"
            alignItems="center"
            className={classes.container}
        >
            <Grid item xs={12} sm={6}>
                <form onSubmit={handleSubmit} className={classes.formContainer}>
                    <Typography variant="h6" gutterBottom center>
                        Login
                    </Typography>
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        id="email"
                        label="Email Address"
                        name="email"
                        autoComplete="email"
                        autoFocus
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        name="password"
                        label="Password"
                        type="password"
                        id="password"
                        autoComplete="current-password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    {error && (
                        <Typography variant="body2" color="error" gutterBottom>
                            {error}
                        </Typography>
                    )}
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        backgroundColor="black"
                        color="white"
                    >
                        Login
                    </Button>
                    <Typography variant="body1" align="center" gutterBottom>
                        Don't have an account? <Link to="/signup">Sign Up</Link>
                    </Typography>
                </form>
            </Grid>
        </Grid>
    );
};

export default Login;
