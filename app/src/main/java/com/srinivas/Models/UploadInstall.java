package com.srinivas.Models;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class UploadInstall {

    @SerializedName("status")
    JsonElement status;

    @SerializedName("message")
    String message;
}
