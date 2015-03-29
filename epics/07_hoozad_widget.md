# 07 - e-commerce widget

## Description
As buyer I want to use the hoozad widget integrated in a e-commerce platform to send a product to a person from whom I do not know the delivery details

## Acceptance criteria

### 1 - As buyer using the widget I want to specify the user-locator

**Given** I'm in the delivery details selection of the e-commerce platform  
**And given** I have selected the hoozad widget  
**When** I set the recipient user-locator  
**Then** I can submit the widget  

### 2 - As buyer using the widget, if the recipient is not found, I want to specify another user-locator

**Given** I'm in the delivery details selection of the e-commerce platform  
**And given** I have tried the hoozad widget with b@mail.com email address as user-locator  
**When** The widget replies with "user-locator not found"  
**Then** I set @bTwitter twitter as recipient user-locator  
**And then** I can submit the widget  ccc

### 3 - As buyer using the widget, if the recipient is not found, I want to send a sign-up request to the receiver

**Given** I'm in the delivery details selection of the e-commerce platform  
**And given** I have tried the hoozad widget with b@mail.com email address as user-locator  
**When** The widget replies with "user-locator not found"  
**Then** I can send a sign-up request through the widget  

### 4 - As buyer using the widget, if the recipient is not found, I want to send a sign-up request to the receiver with a custom message

**Given** I'm in the delivery details selection of the e-commerce platform  
**And given** I have tried the hoozad widget with b@mail.com email address as user-locator  
**When** The widget replies with "user-locator not found"  
**Then** I set a sign-up request custom message 
**And then** I can send a sign-up request through the widget  
