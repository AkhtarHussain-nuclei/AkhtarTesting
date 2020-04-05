Feature: Rename a Channel

  @TC0013 @Plivo @RenameChannel
  Scenario: TC0013- Rename a Channel

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
    And get the channel.id from the response

#    Rename a newly Created Channel
    Given Request headers
      |Key|Value|
      |Content-Type|application/json|
      |Content-Type|application/x-www-form-urlencoded|
      |Authorization | valid        |
    And Request Body Form Data
      |Key|Value|
      |channel|response|
      |name|randomString|
    When user hits "channels.rename" post api
    Then the status code is 200

#  Validate if the channel name has been updated or not by passing its id.
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
      |channel.name|valueOfRandomString|

#    Validate if updated name present in List of Channels
    Given Request headers
      |Key|Value|
      |Content-Type|application/json|
      |Authorization | valid        |
    When user hits "channels.list" get api
    Then the status code is 200
   And Verify if valueOfRandomString present in response body


#    Delete the channel
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




