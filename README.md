# Проект для Android и внешнего устройства (Arduiino)

## Описание

Данное приложение содержит функционал для взаимодействия с внешним устройством. Для этого визульно реализовано 2 кнопки для включения и выключения красного диода на внешнем устройстве.
Код для внешнего устройства написан на C++ и взаимодействует с android через Serial

## Код для внешнего устройства

```cpp
#include "HardwareSerial.h"

#define GPIO_PIN 33

void setup() {
    Serial.begin(9600); // Start communication at 9600 bps
    pinMode(GPIO_PIN, OUTPUT);
}

void loop() {
  if (Serial.available() > 0) {
    char received = Serial.read();

    if (received == '1') {
        digitalWrite(GPIO_PIN, LOW); // Turn off GPIO 33
    } else if (received == '0') {
        digitalWrite(GPIO_PIN, HIGH); // Turn on GPIO 33
    }
  }
}
```

