Feature: Create a new Slack Channel Test Cases

    @TC001 @Plivo @CreateNewChannelCases
    Scenario: TC001-Create New Slack Channels with valid details
      Given Request headers
        |Key|Value|
        |Content-Type|application/json|
        |Content-Type|application/x-www-form-urlencoded|
        |Authorization | valid        |
      And Request Body Form Data
        |Key|Value|
        |name|randomString|
      When user hits "channels.create" post api
      Then the status code is 200
      And Returned json schema is "PostCreateChannelSchema.json"
      And get the channel.id from the response

#      Verify the Channel created by hitting the Get Channel API with the id from above response
      Given Request headers
        |Key|Value|
        |Content-Type|application/json|
        |Authorization | valid        |
      And Request params
        |Key|Value|
        |channel|response|
      When user hits "channels.info" get api
      Then the status code is 200
      And Response should contain
        |Key|Value|
        |channel.id | response|

#      Delete/Archive the Channel created by passing the above Channel id.
      Given Request headers
        |Key|Value|
        |Content-Type|application/json|
        |Content-Type|application/x-www-form-urlencoded|
        |Authorization | valid        |
      And Request Body Form Data
        |Key|Value|
        |channel|response|
      When user hits "channels.archive" post api
      Then the status code is 200
      And Response should contain
        |Key|Value|
        |ok |true |













