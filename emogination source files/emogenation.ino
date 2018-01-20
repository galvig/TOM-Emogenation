
#include <SoftwareSerial.h>

SoftwareSerial btSerial(10, 11); // RX, TX
int LED_R_G[5][2] = {{12,13}, {8,9}, {6,7}, {5,4}, {2,3}};
int BUTTON[5] = {A1, A2, A3, A4, A5};
int previousBtnState[5] = {LOW, LOW, LOW, LOW, LOW};


void setup() {
  Serial.begin(9600);
  btSerial.begin(9600);

  // Button lines are set to OUTPUT since these are analog lines
  for (int i=0; i<5; i++)
    pinMode(BUTTON[i], OUTPUT);
  
  for (int i=0; i<5; i++) {
    pinMode(LED_R_G[i][0], OUTPUT); // R line
    pinMode(LED_R_G[i][1], OUTPUT); // G line
  }
}

void loop() {
  int ledNumber;
  // test 5 time the commands in case all 5 leds got command
  for (int i=0;i<5;i++) {
    if (btSerial.available()) {
      int asciiValue = btSerial.read();
      // validate that an ascii representing led valid number between 1-5
      ledNumber = asciiValue - '0';
      if (ledNumber < 0 || ledNumber > 4) {
        Serial.println(" ERROR in receiving BT info. Illegal ledNumber.");
        continue;  
      } else {  

        if (btSerial.available()) {
          int ledCommand = btSerial.read();
          switch (ledCommand) {
            case 'R' :
              digitalWrite(LED_R_G[ledNumber][0], HIGH);
              digitalWrite(LED_R_G[ledNumber][1], LOW);
              break;
            case 'G' :
              digitalWrite(LED_R_G[ledNumber][0], LOW);
              digitalWrite(LED_R_G[ledNumber][1], HIGH);
              break;
            case '0' :
              digitalWrite(LED_R_G[ledNumber][0], LOW);
              digitalWrite(LED_R_G[ledNumber][1], LOW);
              break;
            default:
              //illegal value
              Serial.println("ERROR in receiving BT info. Illegal ledCommand");
              
          }
        }
      }
    }
  }

  // handle buttons state 
  // send only button number when pressed (like onClick() )
  int currentButtonState;
  for (int i=0; i<5; i++) {
      currentButtonState = digitalRead(BUTTON[i]);
      if (currentButtonState != previousBtnState[i]) { //change in state
        if (currentButtonState == HIGH) {
          char value = '0'+i;
          btSerial.println(value); // send the ascii of the pressed button
          Serial.print(value); // send the ascii of the pressed button
        }
        previousBtnState[i] = currentButtonState;
      }
  }

  delay(30);        // delay in between reads for stability     
}