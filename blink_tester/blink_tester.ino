
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
    
              digitalWrite(LED_R_G[1][0], HIGH);
              digitalWrite(LED_R_G[4][0], HIGH);
              digitalWrite(LED_R_G[0][0], HIGH);
              digitalWrite(LED_R_G[2][0], HIGH);
              digitalWrite(LED_R_G[3][0], HIGH);

}
