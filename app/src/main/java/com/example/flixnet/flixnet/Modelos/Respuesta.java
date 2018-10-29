package com.example.flixnet.flixnet.Modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Respuesta {
    @Expose @SerializedName("error") private boolean error;

    @Expose @SerializedName("message") private String message;

    @Expose @SerializedName("data") private Usuario data;

    public Respuesta(){}

    public Respuesta(boolean error, String message, Usuario data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public boolean isError() { return error; }

    public void setError(boolean error) { this.error = error; }

    public void setData(Usuario data) { this.data = data; }

    public void setMessage(String message) { this.message = message; }

    public Usuario getData() { return data; }

    public String getMessage() { return message; }
}
