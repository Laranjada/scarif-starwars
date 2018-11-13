# scarif-starwars
Projeto desafio b2w: Micro-serviço, Dropwizard, hk2, testcontainer, swagger, mongodb.

**<h3>Para execução do applicativo localmente</h3>**
- Executar o script para start do container MongoDB
- Caso não vá ser usado docker editar as propriedes de conexão com banco no config.yml
    - repository:
        - host: localhost
        - port: 27017
        - user: user
        - passwd: mypass
        - db: starwars
        - collectionName: planetas

**<h3>Para execução dos testes unitarios</h3>**
>Obrigatório intalação do Docker