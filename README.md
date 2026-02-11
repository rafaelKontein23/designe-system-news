# Design System News 📰

[![](https://jitpack.io/v/rafaelKontein23/designe-system-news.svg)](https://jitpack.io/#rafaelKontein23/designe-system-news)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Biblioteca de componentes UI para Android com Design System personalizado, focada em acessibilidade e facilidade de uso.

## 📑 Índice

- [✨ Destaques](#-destaques)
- [🚀 Quick Start](#-quick-start)
- [📦 Instalação](#-instalação)
- [🎨 Componentes Disponíveis](#-componentes-disponíveis)
  - [DsButton](#1-dsbutton)
  - [DsInput](#2-dsinput)
  - [DsText](#3-dstext)
  - [DsChip](#4-dschip)
  - [DsChipGroup](#5-dschipgroup)
  - [DsToolbar](#6-dstoolbar)
  - [DsNotificationCard](#7-dsnotificationcard)
  - [DsIcon](#8-dsicon)
  - [DsNewsCard](#9-dsnewscard)
- [🎨 Recursos de Acessibilidade](#-recursos-de-acessibilidade)
- [📝 Exemplo Completo](#-exemplo-completo)
- [💡 Dicas de Uso](#-dicas-de-uso)
- [🎯 Recursos Avançados](#-recursos-avançados)
- [❓ FAQ](#-faq-perguntas-frequentes)
- [🔧 Troubleshooting](#-troubleshooting)
- [🤝 Contribuindo](#-contribuindo)
- [📊 Versões](#-versões)
- [📄 Licença](#-licença)

---

- 🎨 **10+ componentes** prontos para uso
- ♿ **Acessibilidade completa** com suporte ao TalkBack
- 🎯 **Type-safe** com Kotlin
- 📱 **Material Design 3** como base
- 🚀 **Fácil personalização** de cores e estilos
- 🔧 **ViewBinding** integrado
- 📐 **Menu lateral (Drawer)** integrado na toolbar
- 🔔 **Badges numéricos** em ícones e botões

---

## 🚀 Quick Start

```kotlin
// 1. Configure a Toolbar com drawer
binding.toolbar.apply {
    setToolbarTitle("Minha App", DsText.TextStyle.HEADER)
    setHamburgerMenu {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }
}

// 2. Use botões estilizados
binding.btnLogin.apply {
    setButtonType(DsButton.ButtonType.PRIMARY)
    setDsText("Entrar")
    setDsClickListener { /* ação */ }
}

// 3. Inputs com máscaras automáticas
binding.inputCpf.setKeyboardType(DsInput.KeyboardType.CPF)
binding.inputEmail.setKeyboardType(DsInput.KeyboardType.EMAIL)

// 4. Grupos de chips com seleção única
val categories = listOf("Tecnologia", "Esportes", "Política")
binding.chipGroup.apply {
    addChips(categories)
    selectChip(0) // Seleciona primeiro
}

// 5. Cards de notícia com um método
binding.newsCard.setNews(
    title = "Título da Notícia",
    description = "Descrição completa...",
    time = "Há 2 horas"
)
```

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
        maven { url = uri("https://jitpack.io") } // Adicione esta linha
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

> ⚠️ **Importante:** Verifique a versão mais recente em [JitPack](https://jitpack.io/#rafaelKontein23/designe-system-news)

### 3. Sincronize o projeto

Clique em **Sync Now** no Android Studio ou execute:

```bash
./gradlew build
```

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

### 5. DsChipGroup

Grupo de chips com layout flexível e seleção única (comportamento tipo radio button).

#### Características
- ✅ Seleção única (apenas um chip por vez)
- ✅ Wrap automático (quebra de linha quando não cabe)
- ✅ Espaçamento configurável entre chips
- ✅ Cores personalizáveis para estados selecionado/não selecionado
- ✅ Callbacks de seleção com informações detalhadas
- ✅ Métodos para controle programático da seleção
- ✅ Suporte a acessibilidade completo

#### Uso no XML

```xml
<com.example.mylibrary.ds.chip.DsChipGroup
    android:id="@+id/chipGroup"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:chipSpacing="12dp"
    app:selectedBackgroundColor="@android:color/holo_green_dark"
    app:selectedTextColor="@android:color/white"
    app:unselectedBackgroundColor="@android:color/darker_gray"
    app:unselectedTextColor="@android:color/white" />
```

#### Uso programático

```kotlin
// Adicionar chips usando lista de strings
val categories = listOf(
    "Tecnologia",
    "Esportes",
    "Política",
    "Entretenimento",
    "Negócios",
    "Ciência"
)
binding.chipGroup.addChips(categories)

// Configurar listener de seleção
binding.chipGroup.setOnChipSelectionListener(
    object : DsChipGroup.OnChipSelectionListener {
        override fun onChipSelected(
            chip: DsChip,
            position: Int,
            isSelected: Boolean
        ) {
            if (isSelected) {
                val categoryName = categories[position]
                Toast.makeText(
                    this@MainActivity,
                    "Categoria: $categoryName",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
)

// Selecionar programaticamente
binding.chipGroup.selectChip(0) // Seleciona o primeiro chip

// Outros métodos úteis
binding.chipGroup.clearSelection() // Limpa a seleção
val selectedIndex = binding.chipGroup.getSelectedChipIndex() // Retorna índice (-1 se nenhum)
val selectedChip = binding.chipGroup.getSelectedChip() // Retorna chip selecionado (null se nenhum)

// Personalizar cores programaticamente
binding.chipGroup.setChipColors(
    selectedBg = Color.GREEN,
    selectedText = Color.WHITE,
    unselectedBg = Color.GRAY,
    unselectedText = Color.WHITE
)

// Adicionar chips individualmente
val chip = DsChip(context).apply {
    setDsText("Novo Chip")
}
binding.chipGroup.addChip(chip)

// Ajustar espaçamento dinamicamente
binding.chipGroup.setChipSpacing(16) // 16dp entre chips
```

#### Exemplo de uso com filtros

```kotlin
// Cenário: filtrar notícias por categoria
val categories = listOf("Todas", "Tecnologia", "Esportes", "Política")
binding.filterChipGroup.apply {
    addChips(categories)
    
    setOnChipSelectionListener(object : DsChipGroup.OnChipSelectionListener {
        override fun onChipSelected(chip: DsChip, position: Int, isSelected: Boolean) {
            if (isSelected) {
                when (position) {
                    0 -> loadAllNews()
                    1 -> loadNewsByCategory("tech")
                    2 -> loadNewsByCategory("sports")
                    3 -> loadNewsByCategory("politics")
                }
            }
        }
    })
    
    selectChip(0) // Inicia com "Todas" selecionado
}
```

---

### 6. DsToolbar

Toolbar customizada com suporte a título, botão de voltar, menu hambúrguer (drawer) e até 2 botões de ação com badges.

#### Recursos
- ✅ Título centralizado ou alinhado à esquerda
- ✅ Botão de voltar personalizado
- ✅ Menu hambúrguer para drawer lateral
- ✅ Até 2 botões de ação com badges de notificação
- ✅ Ícones com tamanho consistente (24x24dp)

#### Uso no XML

```xml
<com.example.mylibrary.ds.toolbar.DsToolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

#### Configurando título

```kotlin
binding.toolbar.apply {
    setToolbarTitle(
        titleText = "Meu App",
        textStyle = DsText.TextStyle.HEADER,
        centered = false // true para centralizar
    )
}
```

#### Menu Hambúrguer com Drawer Lateral

Para implementar um menu lateral deslizante:

**1. Estruture o layout com DrawerLayout:**

```xml
<androidx.drawerlayout.widget.DrawerLayout
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- Conteúdo principal -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">
        
        <com.example.mylibrary.ds.toolbar.DsToolbar
            android:id="@+id/toolbar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />
        
        <!-- Resto do conteúdo -->
    </LinearLayout>

    <!-- Menu lateral -->
    <com.google.android.material.navigation.NavigationView
        android:id="@+id/nav_view"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        app:headerLayout="@layout/nav_header"
        app:menu="@menu/drawer_menu" />

</androidx.drawerlayout.widget.DrawerLayout>
```

**2. Configure o menu hambúrguer na Activity:**

```kotlin
import androidx.core.view.GravityCompat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setToolbarTitle("Notícias", DsText.TextStyle.HEADER)
            
            // Abre o drawer ao clicar no hambúrguer
            setHamburgerMenu {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Trata cliques nos itens do menu
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    // Navegar para home
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.menu_favorites -> {
                    // Navegar para favoritos
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
    }
}
```

#### Botão de Voltar

```kotlin
binding.toolbar.setBackButton(show = true) {
    finish() // ou onBackPressed()
}
```

#### Botões de Ação com Badge

```kotlin
binding.toolbar.setActionButtons(
    // Primeiro botão (notificações)
    action1Icon = R.drawable.ds_icon_notification,
    action1BadgeCount = 5, // Número no badge (0 = sem badge)
    action1Content = "Notificações", // Descrição para acessibilidade
    action1BadgeDescription = "notificações não lidas",
    action1Click = {
        Toast.makeText(context, "5 notificações", Toast.LENGTH_SHORT).show()
    },
    
    // Segundo botão (buscar)
    action2Icon = R.drawable.ds_icon_search,
    action2Click = {
        // Abrir tela de busca
    }
)
```

---

### 7. DsNotificationCard

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

### 8. DsIcon

Ícone customizado com suporte a badge de notificação numérico.

#### Recursos
- ✅ Badge circular vermelho com contador
- ✅ Suporte a "99+" para números grandes
- ✅ Redimensionamento automático para acomodar o badge

#### Uso no XML

```xml
<com.example.mylibrary.ds.icon.DsIcon
    android:id="@+id/icon_notification"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@drawable/ds_icon_notification"
    app:badgeCount="5" />
```

#### Uso programático

```kotlin
binding.iconNotification.apply {
    setIcon(ContextCompat.getDrawable(context, R.drawable.ds_icon_notification))
    setBadgeCount(12) // Atualiza o contador
}

// Remover badge
binding.iconNotification.setBadgeCount(0)
```

#### Personalização

O badge tem as seguintes características:
- **Cor de fundo:** Vermelho (#FF0000)
- **Cor do texto:** Branco
- **Posição:** Canto superior direito
- **Tamanho:** 44dp de diâmetro
- **Limite:** Exibe "99+" para valores > 99

---

### 9. DsNewsCard

Card de notícia com imagem, título, descrição e timestamp. Totalmente acessível com contentDescription agrupado.

#### Recursos
- ✅ Layout responsivo com imagem à esquerda
- ✅ Imagem arredondada (12dp de raio)
- ✅ Borda sutil (1dp, #E5E7EB)
- ✅ Acessibilidade completa com TalkBack
- ✅ Suporte a carregamento de imagem personalizado

#### Uso no XML

```xml
<com.example.mylibrary.ds.card.news.DsNewsCard
    android:id="@+id/news_card"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:newsTitle="Mercado financeiro reage positivamente"
    app:newsDescription="Investidores mostram otimismo com as novas políticas econômicas"
    app:newsImage="@drawable/news_image"
    app:newsTime="Hoje, 09:15" />
```

#### Uso programático

```kotlin
// Configuração básica
binding.newsCard.setNews(
    title = "Título da Notícia",
    description = "Breve descrição do conteúdo da notícia",
    time = "Há 2 horas"
)

// Com carregamento customizado de imagem (ex: Glide, Coil, Picasso)
binding.newsCard.setNews(
    title = "Título da Notícia",
    description = "Descrição detalhada...",
    time = "10/02/2025, 14:30",
    imageLoader = {
        // Exemplo com Glide
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder)
            .into(this)
    }
)

// Adicionar listener de clique
binding.newsCard.setOnClickListener {
    // Abrir detalhes da notícia
    val intent = Intent(context, NewsDetailActivity::class.java)
    startActivity(intent)
}
```

#### Acessibilidade

O card agrupa automaticamente todas as informações para o TalkBack:
```
"Notícia: Mercado financeiro reage positivamente. 
Investidores mostram otimismo com as novas políticas econômicas. 
Postado em Hoje, 09:15"
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
        setupDrawer()
        setupForm()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setToolbarTitle("Login", DsText.TextStyle.HEADER)
            
            // Menu hambúrguer para abrir drawer
            setHamburgerMenu {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
            
            // Botões de ação
            setActionButtons(
                action1Icon = R.drawable.ds_icon_notification,
                action1BadgeCount = 3,
                action1Click = { 
                    // Abrir notificações
                }
            )
        }
    }
    
    private fun setupDrawer() {
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    // Navegar para home
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.menu_favorites -> {
                    // Navegar para favoritos
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
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
            
            if (password.isEmpty()) {
                binding.inputPassword.error = "Senha obrigatória"
                return@setDsClickListener
            }
            
            // Realizar login
            performLogin(email, password)
        }
    }
}
```

---

## 📋 Índice de Componentes

| Componente | Descrição |
|------------|-----------|
| **DsButton** | Botão com 5 estilos visuais (primary, secondary, outlined, text, danger) |
| **DsInput** | Campo de entrada com máscaras (CPF, telefone, email, senha, etc.) |
| **DsText** | Texto com estilos pré-definidos e suporte a múltiplas cores |
| **DsChip** | Chip personalizado com cores customizáveis |
| **DsChipGroup** | Grupo de chips com seleção única e layout flexível (wrap automático) |
| **DsToolbar** | Toolbar com título, drawer, botão voltar e botões de ação |
| **DsNotificationCard** | Card de notificação com chip "NOVO" |
| **DsIcon** | Ícone com badge de contador numérico |
| **DsNewsCard** | Card de notícia com imagem, título, descrição e horário |

---

## ❓ FAQ (Perguntas Frequentes)

### Como atualizar a biblioteca?

Mude a versão no `build.gradle.kts`:
```kotlin
implementation("com.github.rafaelKontein23.designe-system-news:mylibrary:v1.0.14")
```

### Os componentes são compatíveis com Jetpack Compose?

Não diretamente, mas você pode usar com `AndroidView`:
```kotlin
AndroidView(factory = { context ->
    DsButton(context).apply {
        setButtonType(DsButton.ButtonType.PRIMARY)
        setDsText("Botão")
    }
})
```

### Como personalizar as fontes?

A biblioteca usa Poppins por padrão. Para substituir, adicione suas fontes em `res/font/` com os mesmos nomes.

### O drawer funciona com Navigation Component?

Sim! Combine com `NavController`:
```kotlin
binding.navView.setupWithNavController(navController)
```

---

## 🔧 Troubleshooting

### Erro: "Failed to resolve: com.github.rafaelKontein23..."

**Solução:** Verifique se adicionou o repositório JitPack no `settings.gradle.kts` do **projeto**.

### Máscaras não funcionam no input

**Solução:** Use `setKeyboardType()` programaticamente em vez de definir no XML.

### Drawer não abre

**Verifique:**
1. Se o layout raiz é `DrawerLayout`
2. Se o `NavigationView` tem `android:layout_gravity="start"`
3. Se está usando `GravityCompat.START` no código

### Badge não aparece na toolbar

**Solução:** Certifique-se de que `badgeCount > 0` e que está usando `setActionButtons()` depois de definir a toolbar.

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 📊 Versões

| Versão | Data | Novidades |
|--------|------|-----------|
| v1.0.13 | 2026-02 | Menu lateral (Drawer), DsIcon, DsNewsCard |
| v1.0.12 | 2026-01 | Badges em toolbar, melhorias de acessibilidade |
| v1.0.11 | 2025-12 | DsNotificationCard, máscaras de input |
| v1.0.10 | 2025-12 | Versão inicial com componentes básicos |

---

## 🔗 Links Úteis

### Personalização de Cores

Todos os componentes que aceitam cores podem ser personalizados:

```kotlin
// Via código
binding.button.setDsBackgroundColorResource(R.color.custom_color)
binding.chip.setDsBackgroundColor(Color.parseColor("#FF5722"))

// Via XML
app:backgroundColorDS="@color/primary"
app:textColorDS="@color/white"
```

### Trabalhando com Imagens

Para carregar imagens em `DsNewsCard` com bibliotecas populares:

```kotlin
// Com Glide
binding.newsCard.setNews(
    title = "Título",
    description = "Descrição",
    time = "Há 1 hora",
    imageLoader = {
        Glide.with(context)
            .load(imageUrl)
            .centerCrop()
            .into(this)
    }
)

// Com Coil
binding.newsCard.setNews(
    title = "Título",
    description = "Descrição",
    time = "Há 1 hora",
    imageLoader = {
        load(imageUrl) {
            crossfade(true)
            placeholder(R.drawable.placeholder)
        }
    }
)
```

### Menu do Drawer (res/menu/drawer_menu.xml)

Exemplo de estrutura de menu para o NavigationView:

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <group android:checkableBehavior="single">
        <item
            android:id="@+id/menu_home"
            android:icon="@drawable/ic_home"
            android:title="Início" />
        <item
            android:id="@+id/menu_favorites"
            android:icon="@drawable/ic_favorite"
            android:title="Favoritos" />
        <item
            android:id="@+id/menu_notifications"
            android:icon="@drawable/ic_notifications"
            android:title="Notificações" />
    </group>

    <item android:title="Configurações">
        <menu>
            <item
                android:id="@+id/menu_settings"
                android:icon="@drawable/ic_settings"
                android:title="Configurações" />
            <item
                android:id="@+id/menu_logout"
                android:icon="@drawable/ic_logout"
                android:title="Sair" />
        </menu>
    </item>
</menu>
```

### Header do Drawer (res/layout/nav_header.xml)

Exemplo de header personalizado:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="176dp"
    android:background="?attr/colorPrimary"
    android:gravity="bottom"
    android:orientation="vertical"
    android:padding="16dp">

    <ImageView
        android:id="@+id/nav_header_image"
        android:layout_width="64dp"
        android:layout_height="64dp"
        android:src="@mipmap/ic_launcher_round" />

    <com.example.mylibrary.ds.text.DsText
        android:id="@+id/nav_header_title"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="Nome do Usuário"
        android:textColor="@android:color/white"
        app:dsTextStyle="header" />

    <com.example.mylibrary.ds.text.DsText
        android:id="@+id/nav_header_subtitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="usuario@email.com"
        android:textColor="@android:color/white"
        app:dsTextStyle="description" />

</LinearLayout>
```

---

## 🎯 Recursos Avançados

### Validação de Formulários

```kotlin
fun validateForm(): Boolean {
    var isValid = true
    
    // Email
    if (binding.inputEmail.text.toString().isEmpty()) {
        binding.inputEmail.error = "Email obrigatório"
        isValid = false
    }
    
    // CPF
    val cpf = binding.inputCpf.text.toString()
    if (cpf.isEmpty() || cpf.length < 14) {
        binding.inputCpf.error = "CPF inválido"
        isValid = false
    }
    
    return isValid
}
```

### Navegação com Drawer

```kotlin
private fun setupNavigation() {
    binding.navView.setNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.menu_home -> navigateToHome()
            R.id.menu_favorites -> navigateToFavorites()
            R.id.menu_settings -> navigateToSettings()
            R.id.menu_logout -> performLogout()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        true
    }
}

// Fechar drawer ao pressionar voltar
override fun onBackPressed() {
    if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    } else {
        super.onBackPressed()
    }
}
```

### Atualização Dinâmica de Badges

```kotlin
// Atualizar badge na toolbar
binding.toolbar.setActionButtons(
    action1Icon = R.drawable.ds_icon_notification,
    action1BadgeCount = notificationCount, // Variável dinâmica
    action1Click = { openNotifications() }
)

// Atualizar badge em ícone individual
binding.iconNotification.setBadgeCount(newCount)
```

---

- [JitPack](https://jitpack.io/#rafaelKontein23/designe-system-news)
- [Releases](https://github.com/rafaelKontein23/designe-system-news/releases)
- [Issues](https://github.com/rafaelKontein23/designe-system-news/issues)

---

## 📄 Licença

Este projeto está sob a licença MIT.