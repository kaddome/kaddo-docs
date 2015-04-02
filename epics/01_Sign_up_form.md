# 01 - Sign-up form

## Description
As a non-registered-user I want to register to hoozad to anonymously share my delivery details to receive gifts

## Acceptance criteria

### 1 - As a non-registered-user in the registration form I need to use my Facebook account

**Given** I am in the hoozad registration form page  
**And given** the registration details are not editable  
**And given** I cannot submit the registration form    
**When** I use my facebook login    
**Then** the registration details are editable   

### 2 - As a non-registered-user in the registration form I need to add my delivery details

**Given** I am in the hoozad registration form page  
**And given** I need to add my delivery details     
**When** I add a complete address    
**Then** I can submit the registration form 

### 3 - As a non-registered-user in the registration form I want to receive gifts by any hoozad user  

**Given** I am in the hoozad registration form page  
**And given** I need to select a privacy mode option   
**When** I select the option "Receive from any hoozad user"  
**Then** my delivery details will be shared with any hoozad user  

### 4 - As a non-registered-user in the registration form I want to receive gifts only by facebook connections  

**Given** I am in the hoozad registration form page   
**And given** I need to select a privacy mode option   
**When** I select the option "Receive from facebook connections"  
**Then** my delivery details will be shared with any facebook connection   