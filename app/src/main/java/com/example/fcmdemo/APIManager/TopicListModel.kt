package com.example.fcmdemo.APIManager

import com.fasterxml.jackson.annotation.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.HashMap

//class TopicListModel {
//    @SerializedName("rel")
//    @Expose
//    var rel: Rel = Rel()
//    var applicationVersion: String? = null
//    var application: String? = null
//    var scope: String? = null
//    var authorizedEntity: String? = null
//    var appSigner: String? = null
//    var platform: String? = null
//}
//class Rel {
//    @SerializedName("topics")
//    @Expose
//    var topics: Map<String, addDate> = HashMap<String,addDate>()
//}
//
//
//class addDate {
//    @SerializedName("addDate")
//    @Expose
//    var addDate: String? = ""
//}
import com.fasterxml.jackson.annotation.JsonAnySetter

import com.fasterxml.jackson.annotation.JsonAnyGetter

import com.fasterxml.jackson.annotation.JsonProperty

import com.fasterxml.jackson.annotation.JsonIgnore


import com.fasterxml.jackson.annotation.JsonPropertyOrder

import com.fasterxml.jackson.annotation.JsonInclude
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    "applicationVersion",
    "application",
    "scope",
    "authorizedEntity",
    "rel",
    "appSigner",
    "platform"
)
class TopicListModel {
    @get:JsonProperty("applicationVersion")
    @set:JsonProperty("applicationVersion")
    @JsonProperty("applicationVersion")
    var applicationVersion: String? = null

    @get:JsonProperty("application")
    @set:JsonProperty("application")
    @JsonProperty("application")
    var application: String? = null

    @get:JsonProperty("scope")
    @set:JsonProperty("scope")
    @JsonProperty("scope")
    var scope: String? = null

    @get:JsonProperty("authorizedEntity")
    @set:JsonProperty("authorizedEntity")
    @JsonProperty("authorizedEntity")
    var authorizedEntity: String? = null

    @get:JsonProperty("rel")
    @set:JsonProperty("rel")
    @JsonProperty("rel")
    var rel: Rel? = null

    @get:JsonProperty("appSigner")
    @set:JsonProperty("appSigner")
    @JsonProperty("appSigner")
    var appSigner: String? = null

    @get:JsonProperty("platform")
    @set:JsonProperty("platform")
    @JsonProperty("platform")
    var platform: String? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = HashMap()
    @JsonAnyGetter
    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    @JsonAnySetter
    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("topics")

class Rel {
    @get:JsonProperty("topics")
    @set:JsonProperty("topics")
    @JsonProperty("topics")
    var topics: Map<Any,Any>?=null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = HashMap()
    @JsonAnyGetter
    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    @JsonAnySetter
    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}

//@JsonInclude(JsonInclude.Include.NON_NULL)
//class Topics {
//    @JsonIgnore
//    private val additionalProperties: MutableMap<String, Any> = HashMap()
//    @JsonAnyGetter
//    fun getAdditionalProperties(): Map<String, Any> {
//        return additionalProperties
//    }
//
//    @JsonAnySetter
//    fun setAdditionalProperty(name: String, value: Any) {
//        additionalProperties[name] = value
//    }
//}