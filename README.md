## Изменения
Никаких изменений в логике парсинга нет, 
команды все так же получают список аргументов, и уже дальше сами по себе 
их интерпретируют как просто аргументы или ключи
 

Для парсинга аргументов используется Apache CLI, она достаточно удобная 
в использовании, предоставляет все то, что сейчас нужно для работы с аргументами,
количество кода, необходимое для работы с ней, значительно меньше, чем для других библиотек