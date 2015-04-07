# 09 - complete-profile requests communications

## Description
As sender using the hoozad widget, when I select an existing hoozad user with unknown delivery details I can send a complete-profile request to the user.

## Acceptance criteria

### 1 - As registered-user without delivery details I refuse the complete-profile request 

**Given** an existing hoozad user A sends a complete-profile request to an existing hoozad user B without delivery details  
**When** B rejects the complete-profile request  
**Then** A receives a notification stating that B has not accepted the complete-profile request  

### 2 - As registered-user without delivery details I don't reply to the complete-profile request

**Given** an existing hoozad user A sends a complete-profile request to an existing hoozad user B without delivery details  
**When** after X time, B has not replied the request  
**Then** A receives a notification stating that B has not replied the complete-profile request  

### 3 - As registered-user without delivery details I accept the complete-profile request

**Given** an existing hoozad user A sends a complete-profile request to an existing hoozad user B without delivery details  
**When** B accepts the complete-profile request and fills the complete-profile form  
**Then** A receives a notification stating that B delivery details are available  
