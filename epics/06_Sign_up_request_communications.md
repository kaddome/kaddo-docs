# 06 - sign-up requests communications

## Description
As the hoozad app, when a delivery details request for an unknown person is received, the platform will try to contact the person with a sign-up request

## Acceptance criteria

### 1 - As non-registered-user I refuse the sign-up request 

**Given** an existing hoozad user A sends a sign-up request to a non-existing hoozad user B  
**When** the B rejects the sign-up request  
**Then** the A receives a notification stating that B has not accepted the sign-up request  

### 2 - As non-registered-user I don't reply to the sign-up request

**Given** an existing hoozad user A sends a sign-up request to a non-existing hoozad user B  
**When** after X time, B has not replied the request  
**Then** the A receives a notification stating that B has not replied the sign-up request  

### 3 - As non-registered-user I accept the sign-up request

**Given** an existing hoozad user A sends a sign-up request to a non-existing hoozad user B  
**When** after X time, B has not replied the request  
**Then** the A receives a notification stating that B has accepted the sign-up request  
