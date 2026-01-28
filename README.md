# Design System News

Primeira versão Design System (Android Library)

---

## Como adicionar ao seu projeto

### 1. Adicione o repositório do JitPack

No arquivo `settings.gradle` (**do projeto**, não do módulo):

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

---

### 2. Adicione a dependência

No `build.gradle` (ou `build.gradle.kts`) do seu módulo app, inclua:

```kotlin
implementation("com.github.rafaelKontein23.designe-system-news:mylibrary:v1.0.1")
```

> ⚠️ Sempre confira a versão/tag mais recente no [JitPack](https://jitpack.io/#rafaelKontein23/designe-system-news) ou no seu GitHub Releases.

---

## Exemplo de uso

```xml
<com.example.mylibrary.ds.button.DsButton
        android:id="@+id/btn1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="68dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:textDS="da dabfa" /> // Seu texto aqui
```

---
input

    <com.example.mylibrary.ds.input.DsInput
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:inputKeyboardType="phone" // aqui você passa o tipo do campo quer usar
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent">

    </com.example.mylibrary.ds.input.DsInput>

    Os tipos de campos são esses
        CPF
        NUMBER
        PHONE
        EMAIL
        PASSWORD
        DATE

## Links úteis

- [Página do projeto no JitPack](https://jitpack.io/#rafaelKontein23/designe-system-news)
- [Releases no GitHub](https://github.com/rafaelKontein23/designe-system-news/releases)

---

**Ficou com dúvida? Abra um issue!**
