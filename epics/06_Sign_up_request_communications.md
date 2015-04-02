# 06 - sign-up requests communications

## Description
As sender using the hoozad widget, when I select a nonexistent hoozad user I can send a sign-up request to the user.

## Acceptance criteria

### 1 - As non-registered-user I refuse the sign-up request 

**Given** an existing hoozad user A sends a sign-up request to a non-existing hoozad user B  
**When** B rejects the sign-up request  
**Then** A receives a notification stating that B has not accepted the sign-up request  

### 2 - As non-registered-user I don't reply to the sign-up request

**Given** an existing hoozad user A sends a sign-up request to a non-existing hoozad user B  
**When** after X time, B has not replied the request  
**Then** A receives a notification stating that B has not replied the sign-up request  

### 3 - As non-registered-user I accept the sign-up request

**Given** an existing hoozad user A sends a sign-up request to a non-existing hoozad user B  
**When** B accepts the sign-up request and fills the sign-up form  
**Then** A receives a notification stating that B delivery details are available 
