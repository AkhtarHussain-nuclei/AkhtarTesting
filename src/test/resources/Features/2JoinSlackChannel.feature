Feature: Join a newly Created Slack Channel

  @TC0012 @Plivo @JoinSlackChannel
  Scenario: TC0012-Join a channel
#    Create a Channel
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
    And get the channel.name from the response

#    Join the newly Created Slack Channel
#  Here, the token created doesn't have access to invite a new user (read only access for creating a team).
#  Hence, passed the admin user only (one who created the channel). Ideally, new user should be passed.
    Given Request headers
      |Key|Value|
      |Content-Type|application/json|
      |Content-Type|application/x-www-form-urlencoded|
      |Authorization | valid        |
    And Request Body Form Data
      |Key|Value|
      |name|response|
    When user hits "channels.join" post api
    Then the status code is 200
    And Returned json schema is "PostAddUserToChannelSchema.json"
    And Response should contain
      |Key|Value|
      |ok |true |
      |channel.name|response|
    And get the channel.id from the response

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





