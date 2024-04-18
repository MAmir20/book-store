import React, { useState } from "react";
import { useHistory } from "react-router-dom";

const Signup = () => {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const history = useHistory();

    const handleSubmit = async (e) => {
        e.preventDefault();
        // Implémentez votre logique d'inscription ici, par exemple en utilisant une requête POST à votre API
        try {
            const response = await fetch("http://localhost:8087/api/users/", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    name,
                    email,
                    password,
                }),
            });
            if (response.ok) {
                // Redirigez l'utilisateur vers la page de connexion après l'inscription réussie
                history.push("/login");
            } else {
                // Affichez un message d'erreur si l'inscription échoue
                console.error("Signup failed");
            }
        } catch (error) {
            console.error("Error signing up user:", error);
        }
    };

    return (
        <div>
            <h2>Signup</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                />
                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <button type="submit">Signup</button>
            </form>
        </div>
    );
};

export default Signup;
