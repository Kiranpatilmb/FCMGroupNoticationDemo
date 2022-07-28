package com.example.fcmdemo.APIManager

import com.fasterxml.jackson.annotation.JsonProperty


class Data {
    var customId: String? = null
    var badge = 0
    var alert: String? = null
}

class Notification {
    var title: String? = null
    var body: String? = null
}

class OperationModel {
    @JsonProperty("to")
    var myto: String? = null
    var priority: String? = null
    var notification: Notification? = null
    var data: Data? = null
    var condition:String?=null
}

