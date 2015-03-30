# 07 - e-commerce widget

## Description
As buyer I want to use the hoozad widget integrated in a e-commerce platform to send a product to a person from whom I do not know the delivery details

## Acceptance criteria

### 1 - As buyer using the widget I want to specify the FB account to send

**Given** I'm in the delivery details selection of the e-commerce platform  
**And given** I have selected the hoozad widget  
**And given** I have login to hoozad with my FB account
**When** I set the recipient FB account
**Then** I can submit the widget  

### 2 - As buyer using the widget, if the recipient is not found, I want to send a sign-up request to the receiver

**Given** I'm in the delivery details selection of the e-commerce platform  
**And given** I have tried the hoozad widget with *userB_fb* FB account  
**When** The widget replies with "delivery details not found"  
**Then** I can send a sign-up request through the widget  

### 3 - As buyer using the widget, if the recipient is not found, I want to send a sign-up request to the receiver with a custom message

**Given** I'm in the delivery details selection of the e-commerce platform  
**And given** I have tried the hoozad widget with *userB_fb* FB account  
**When** The widget replies with "delivery details not found"  
**Then** I set a sign-up request custom message 
**And then** I can send a sign-up request through the widget  
