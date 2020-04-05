Feature: Archive a Channel

  @TC0014 @Plivo @CreateNewChannelCases
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
    And get the channel.id from the response

#    Archive the Channel

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

#    Get the channel and validate the is_archived flag
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
      |channel.is_archived | true|
