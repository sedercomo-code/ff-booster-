# FF Booster Pro — projeto Android

Projeto nativo inicial para Android 8+ com monitor básico de RAM/armazenamento, abertura do Free Fire e verificação/autorização do Shizuku.

## Importante: limites reais
- O app não promete FPS fixo, redução garantida de ping, mira automática, dano, nem overclock.
- Android/HyperOS não permite que um app comum limpe RAM de forma persistente ou altere limites térmicos.
- Shizuku concede acesso elevado somente após autorização explícita; esta versão apenas verifica a conexão e a permissão. Não executa comandos destrutivos nem encerra serviços.
- O monitor usa métricas públicas do Android e pode diferir dos números exibidos pelo HyperOS.

## Como compilar
1. Instale Android Studio em um computador.
2. Abra a pasta `FF_Booster_Pro` como projeto.
3. Aguarde a sincronização do Gradle (é necessária internet para baixar dependências).
4. Use Build > Build APK(s).
5. O APK de depuração ficará em `app/build/outputs/apk/debug/app-debug.apk`.

## Shizuku no celular
1. Instale Shizuku pela fonte oficial.
2. Ative Opções do desenvolvedor e Depuração sem fio.
3. Inicie o Shizuku pelo pareamento/depuração sem fio seguindo as instruções do próprio Shizuku.
4. Abra FF Booster Pro e toque em “Conectar / autorizar Shizuku”.

## Pacotes do Free Fire
O app tenta abrir `com.dts.freefireth` (Free Fire) e `com.dts.freefiremax` (Free Fire MAX).


## Gerar o APK sem instalar Android Studio
O ZIP inclui um workflow do GitHub Actions:
1. Crie um repositório no GitHub e envie todos os arquivos desta pasta.
2. Abra a aba **Actions** e execute **Build FF Booster Pro APK** (ou aguarde o build após enviar para `main`).
3. Quando terminar, abra a execução e baixe o artefato **FF-Booster-Pro-APK**.
4. Extraia o artefato no celular e instale `app-debug.apk`. Talvez seja necessário permitir “Instalar apps desconhecidos” para o app usado para abrir o APK.

O APK gerado será de depuração e não publicado na Play Store. A primeira instalação pode pedir autorização de instalação externa.
