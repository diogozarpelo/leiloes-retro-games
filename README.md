# Leilões RetroGames

Aplicativo Android nativo para organizar e acompanhar leilões de jogos e consoles retrô publicados em grupos do Facebook.

## Status do projeto

Em desenvolvimento, com o MVP funcional.

## Objetivo

Centralizar os leilões acompanhados pelo usuário, organizá-los por data de encerramento, exibir o tempo restante e emitir notificações antes do término.

Os lances continuam sendo realizados diretamente na publicação original do Facebook.

## Funcionalidades implementadas

* Cadastro manual de leilões
* Edição e exclusão
* Persistência local com Room
* Listagem de leilões ativos
* Ordenação pelos leilões que encerram primeiro
* Contagem regressiva atualizada em tempo real
* Separação entre leilões ativos e encerrados
* Filtros de leilões encerrados:

    * Todos
    * Não ganho
    * A pagar
    * Pago
* Registro de leilões ganhos e não ganhos
* Registro do valor final
* Controle de itens ganhos a pagar
* Marcação de pagamento concluído
* Estado de conservação:

    * Ótimo
    * Bom
    * Médio
    * Ruim
    * Péssimo
* Seleção de data de encerramento
* Seleção de horário com hora e minuto
* Validações do formulário
* Abertura da publicação original no Facebook
* Notificações locais antes do encerramento:

    * 1 hora
    * 30 minutos
    * 15 minutos
    * 10 minutos
    * 5 minutos
* Testes unitários do modelo

## Tecnologias

* Kotlin
* Jetpack Compose
* Material 3
* Room
* StateFlow
* ViewModel
* Android AlarmManager
* Notificações locais
* Git
* GitHub
* Android 8.0 ou superior — API 26

## Armazenamento

Os dados são armazenados localmente no aparelho utilizando Room.

A versão atual não possui:

* Login
* Servidor
* Conta de usuário
* Sincronização online

## Arquitetura atual

O projeto está organizado em camadas principais:

* `model` — modelos e enums do domínio
* `data/local` — banco de dados Room e DAO
* `data/repository` — acesso aos dados
* `ui/viewmodel` — estado e operações da interface
* `ui/screens` — telas em Jetpack Compose

A navegação ainda é controlada manualmente pela `MainActivity`.

## Roadmap

* [x] Definição do escopo
* [x] Criação do projeto Android
* [x] Configuração do Git e GitHub
* [x] Modelagem dos dados
* [x] Banco de dados local com Room
* [x] Cadastro e edição de leilões
* [x] Listagem de leilões
* [x] Contagem regressiva
* [x] Controle de status e pagamento
* [x] Estado de conservação
* [x] Seleção de data e horário
* [x] Validações do formulário
* [x] Filtros de leilões encerrados
* [x] Notificações locais
* [x] Integração com links do Facebook
* [x] Testes unitários iniciais
* [ ] Melhorar cobertura de testes
* [ ] Melhorar acabamento visual
* [ ] Adicionar capturas de tela ao README
* [ ] Avaliar uso de Navigation Compose
* [ ] Preparar versão de apresentação para portfólio

## Autor

Diogo Antonio Zarpelão
