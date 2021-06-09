class BaseAmsCommunication {

    attachMessageListener() {
        var messageHandlerFunction = this.handleMessage.bind(this);
        window.addEventListener("message", messageHandlerFunction, false);
    }

    sendMessage(message) {
        window.parent.postMessage(message,'*');
    }
    sendRequest(messageOptions, handleMessageResponse, requestId, isProxyRequest, destinationUrl, requestType) {
        this.receivedMessage = false;
        this.handleMessageResponse = handleMessageResponse;
        var message;
        if(isProxyRequest === true){
            message = {amsAction : this.amsAction, requestId : requestId, destinationUrl : destinationUrl, requestType : requestType};
        }
        else {
            message = {amsAction : this.amsAction, requestId : requestId};
        }
        message = Object.assign(message, messageOptions);
        this.sendMessage(message);

    }
    handleMessage(event) {
        if(event && event.data) {
            console.log("Received Event From AMS", event);
            var data = event.data;
            if(data.requestId == this.amsAction || data.configurationStatus){
                this.receivedMessage = true;
                var response = this.parseResponse ? this.parseResponse(data) : data;
                this.handleMessageResponse(response);
            }  
        }
    }
}
  
class AmsAppConfigurationSubmitter extends BaseAmsCommunication{
    constructor() {
        super();
        this.amsAction = "saveappconfig";
    }
    sendRequest(jsonValues, handleSubmissionResponse) {
        super.sendRequest({payload: jsonValues}, handleSubmissionResponse);
    }
}