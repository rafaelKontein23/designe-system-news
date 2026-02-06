# Design System News

Biblioteca de componentes UI para Android com Design System personalizado.

---

## 📦 Instalação

### 1. Adicione o repositório JitPack

No arquivo `settings.gradle.kts` (**do projeto**, não do módulo):

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. Adicione a dependência

No `build.gradle.kts` do módulo `app`:

```kotlin
dependencies {
    implementation("com.github.rafaelKontein23.designe-system-news:mylibrary:v1.0.13")
}
```

> ⚠️ Verifique a versão mais recente em [JitPack](https://jitpack.io/#rafaelKontein23/designe-system-news)

---

## 🎨 Componentes Disponíveis

### 1. DsButton

Botão customizado com 5 estilos visuais pré-definidos.

#### Tipos disponíveis
- `primary` - Fundo azul, texto branco
- `secondary` - Fundo cinza, texto branco
- `outlined` - Borda azul, fundo transparente
- `text` - Sem fundo, texto azul
- `danger` - Fundo vermelho, texto branco

#### Uso no XML

```xml
<com.example.mylibrary.ds.button.DsButton
    android:id="@+id/btn_primary"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:buttonType="primary"
    app:textDS="Botão Primário"
    app:iconStart="@drawable/ic_icon"
    app:backgroundColorDS="@color/custom_color"
    app:textColorDS="@color/custom_text_color" />
```

#### Uso programático

```kotlin
binding.btnPrimary.apply {
    setButtonType(DsButton.ButtonType.PRIMARY)
    setDsText("Clique aqui")
    setDsBackgroundColorResource(R.color.black)
    setDsClickListener {
        Toast.makeText(context, "Botão clicado!", Toast.LENGTH_SHORT).show()
    }
}
```

---

### 2. DsInput

Campo de entrada com suporte a máscaras e validação.

#### Tipos de teclado
- `text` - Teclado padrão
- `number` - Apenas números
- `email` - Email com validação
- `password` - Senha com toggle de visibilidade
- `cpf` - Máscara ###.###.###-##
- `phone` - Máscara (##) #####-####
- `date` - Máscara ##/##/####

#### Uso no XML

```xml
<com.example.mylibrary.ds.input.DsInput
    android:id="@+id/input_email"
    android:layout_width="match_parent"
    android:layout_height="56dp"
    android:hint="Digite seu email"
    app:inputKeyboardType="email" />
```

#### Uso programático

```kotlin
binding.inputEmail.apply {
    setKeyboardType(DsInput.KeyboardType.EMAIL)
    setText("usuario@email.com")
    error = "Email inválido" // Exibir erro
    error = null // Remover erro
}
```

---

### 3. DsText

Componente de texto com estilos e pesos de fonte personalizados.

#### Estilos disponíveis
- `header` - 24sp, Bold
- `subtitle` - 14sp, SemiBold
- `description` - 14sp, Medium
- `text` - 12sp, Regular

#### Pesos de fonte
- `regular` - Poppins Regular
- `medium` - Poppins Medium
- `semi_bold` - Poppins SemiBold
- `bold` - Poppins Bold

#### Uso no XML

```xml
<com.example.mylibrary.ds.text.DsText
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Título Principal"
    app:dsTextStyle="header" />
```

#### Uso programático

```kotlin
binding.textTitle.apply {
    setTextStyle(DsText.TextStyle.HEADER)
    setFontWeight(DsText.FontWeight.BOLD)
    
    // Texto com múltiplas cores
    setColoredText(
        "Ainda não tem conta? " to Color.GRAY,
        "Cadastrar" to Color.BLUE
    )
}
```

---

### 4. DsChip

Componente de chip com bordas arredondadas e cores personalizáveis.

#### Uso no XML

```xml
<com.example.mylibrary.ds.chip.DsChip
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:chipText="NOVO"
    app:chipBackgroundColorDS="@android:color/holo_blue_dark"
    app:chipTextColorDS="@android:color/white" />
```

#### Uso programático

```kotlin
binding.chipNew.apply {
    setDsText("PROMOÇÃO")
    setDsBackgroundColor(Color.RED)
    setDsTextColor(Color.WHITE)
}
```

---

### 5. DsToolbar

Toolbar customizada com suporte a título, botão de voltar e até 2 botões de ação.

#### Uso no XML

```xml
<com.example.mylibrary.ds.toolbar.DsToolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

#### Uso programático

```kotlin
binding.toolbar.apply {
    // Título centralizado ou alinhado à esquerda
    setToolbarTitle(
        titleText = "Meu App",
        textStyle = DsText.TextStyle.HEADER,
        centered = false
    )
    
    // Botão de voltar
    setBackButton(show = true) {
        finish()
    }
    
    // Botões de ação com badge
    setActionButtons(
        action1Icon = R.drawable.ds_icon_notification,
        action1BadgeCount = 5, // Badge com contador
        action1Click = {
            Toast.makeText(context, "Notificações", Toast.LENGTH_SHORT).show()
        },
        action2Icon = R.drawable.ds_icon_search,
        action2Click = {
            Toast.makeText(context, "Buscar", Toast.LENGTH_SHORT).show()
        }
    )
}
```

---

### 6. DsNotificationCard

Card de notificação com título, data/hora e chip "NOVO".

#### Uso no XML

```xml
<com.example.mylibrary.ds.card.notification.DsNotificationCard
    android:id="@+id/notification_card"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:notificationTitle="Nova mensagem recebida"
    app:notificationDateTime="10/02/2025 às 14:30"
    app:isNew="true" />
```

#### Uso programático

```kotlin
binding.notificationCard.apply {
    setTitle("Atualização disponível")
    setDateTime("Hoje às 10:00")
    setIsNew(true)
    
    // Personalizar chip
    setChipText("URGENTE")
    setChipBackgroundColor(Color.RED)
    setChipTextColor(Color.WHITE)
    
    // Adicionar listener de clique
    setOnClickListener {
        Toast.makeText(context, "Card clicado", Toast.LENGTH_SHORT).show()
    }
}
```

---

## 🎨 Recursos de Acessibilidade

Todos os componentes incluem:
- ✅ Suporte completo ao TalkBack
- ✅ Descrições de conteúdo apropriadas
- ✅ Navegação por toque exploratório
- ✅ Ações de acessibilidade personalizadas (ex: toggle de senha)

---

## 📝 Exemplo Completo

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupForm()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setToolbarTitle("Login", DsText.TextStyle.HEADER)
            setBackButton(show = true) { finish() }
        }
    }

    private fun setupForm() {
        binding.btnLogin.setDsClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            
            if (email.isEmpty()) {
                binding.inputEmail.error = "Email obrigatório"
                return@setDsClickListener
            }
            
            // Realizar login
        }
    }
}
```

---

## 🔗 Links Úteis

- [JitPack](https://jitpack.io/#rafaelKontein23/designe-system-news)
- [Releases](https://github.com/rafaelKontein23/designe-system-news/releases)
- [Issues](https://github.com/rafaelKontein23/designe-system-news/issues)

---

## 📄 Licença

Este projeto está sob a licença MIT.