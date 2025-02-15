# Tauri 插件 TinysInternalFS / 内置存储管理器

# 重要声明

本插件主要为 Tinywang 开发 Tauri 应用程序提供便利，仅根据个人需求定制。

目前，仅提供 Android 实现。iOS 支持正在计划中。

本项目仍处于开发阶段，可能不稳定。

## 介绍

此 Tauri 插件实现了 Android 上的内部存储功能 ( `/data/data/app.tauri` )，无需任何权限。

**主要特性:**

*   **`TFile` 类**:  提供基于文件句柄的方法来进行顺序文件操作。通过使用写入队列，确保操作按顺序执行，特别是对于写入操作。
*   **立即文件操作**: 提供用于快速读取和写入操作的函数 (`readFileImmediately`, `writeFileImmediately`)，无需显式管理文件句柄。
*   **文件存在性检查**: 允许您验证给定路径的文件是否存在 (`checkFileExists`)。
*   **关闭所有文件**: 提供关闭插件打开的所有文件的功能 (`closeFileAll`)。

**已实现功能:**

- **使用 `TFile` 类:**
    - 以指定模式（例如，读取，写入）打开文件
    - 顺序写入内容到文件 (`write`)
    - 从文件读取所有内容 (`readAll`)
    - 关闭文件 (`close`)
- **立即文件操作:**
    - 立即读取文件 (`readFileImmediately`)
    - 立即写入文件 (`writeFileImmediately`)
- **实用工具:**
    - 检查文件是否存在 (`checkFileExists`)
    - 关闭所有文件 (`closeFileAll`)


## 库文件操作函数实现状态表

|  函数名称 (模块/类)          | Android | iOS | Windows | macOS | Linux |
|------------------------------------------|---------|-----|---------|-------|-------|
| `TFile` 类                             | ✅      | ❌   | ❌      | ❌     | ❌     |
| `TFile.write()`                           | ✅      | ❌   | ❌      | ❌     | ❌     |
| `TFile.readAll()`                         | ✅      | ❌   | ❌      | ❌     | ❌     |
| `TFile.close()`                           | ✅      | ❌   | ❌      | ❌     | ❌     |
| `readFileImmediately()`                   | ✅      | ❌   | ❌      | ❌     | ❌     |
| `writeFileImmediately()`                  | ✅      | ❌   | ❌      | ❌     | ❌     |
| `checkFileExists()`                       | ✅      | ❌   | ❌      | ❌     | ❌     |
| `closeFileAll()`                          | ✅      | ❌   | ❌      | ❌     | ❌     |

**注释:**

*   ✅  : 已实现
*   ❌  : 未实现

## 用法

### 使用 `TFile` 类进行文件操作

`TFile` 类专为需要在受控和顺序方式下对文件执行多项操作的场景而设计。它特别适用于确保写入操作按照调用顺序执行。

**打开文件并写入内容:**

```typescript
import { TFile } from 'plugin-tinys-internal-fs-api';

async function writeFileExample() {
  try {
    const filePath = 'my_internal_file.txt';
    const file = new TFile(filePath, 'write'); // 以 'write' 模式打开文件
    await file.waitUntilInitialized(); // 确保文件已打开

    await file.write('第一行内容。\n');
    await file.write('第二行内容。\n');
    await file.write('第三行内容。\n');

    await file.close(); // 写入完成后关闭文件
    console.log('使用 TFile 类成功写入文件。');

  } catch (error) {
    console.error('使用 TFile 写入文件失败:', error);
  }
}

writeFileExample();
```

**打开文件并读取所有内容:**

```typescript
import { TFile } from 'plugin-tinys-internal-fs-api';

async function readFileExample() {
  try {
    const filePath = 'my_internal_file.txt';
    const file = new TFile(filePath, 'read'); // 以 'read' 模式打开文件
    await file.waitUntilInitialized(); // 确保文件已打开

    const content = await file.readAll();
    console.log('文件内容:', content);

    await file.close(); // 读取后关闭文件 (释放资源很重要)

  } catch (error) {
    console.error('使用 TFile 读取文件失败:', error);
  }
}

readFileExample();
```

### 使用立即函数进行快速操作

对于简单的一次性读取或写入操作，您可以使用立即函数 `readFileImmediately` 和 `writeFileImmediately`。

**立即写入内容:**

```typescript
import { writeFileImmediately } from 'plugin-tinys-internal-fs-api';

async function writeImmediatelyExample() {
  try {
    const filePath = 'quick_write_file.txt';
    await writeFileImmediately(filePath, '立即写入的内容!');
    console.log('文件已立即写入。');
  } catch (error) {
    console.error('立即写入文件失败:', error);
  }
}

writeImmediatelyExample();
```

**立即读取内容:**

```typescript
import { readFileImmediately } from 'plugin-tinys-internal-fs-api';

async function readImmediatelyExample() {
  try {
    const filePath = 'quick_write_file.txt'; // 假设此文件存在
    const content = await readFileImmediately(filePath);
    console.log('立即读取的文件内容:', content);
  } catch (error) {
    console.error('立即读取文件失败:', error);
  }
}

readImmediatelyExample();
```

### 检查文件是否存在

```typescript
import { checkFileExists } from 'plugin-tinys-internal-fs-api';

async function checkFileExistenceExample() {
  try {
    const filePath = 'my_internal_file.txt';
    const exists = await checkFileExists(filePath);
    if (exists) {
      console.log(`文件 "${filePath}" 存在。`);
    } else {
      console.log(`文件 "${filePath}" 不存在。`);
    }
  } catch (error) {
    console.error('检查文件存在性失败:', error);
  }
}

checkFileExistenceExample();
```

### 关闭所有已打开的文件

在您的应用程序即将终止或完成文件操作后，最好关闭所有文件以释放资源。

```typescript
import { closeFileAll } from 'plugin-tinys-internal-fs-api';

async function closeAllFilesExample() {
  try {
    await closeFileAll();
    console.log('所有文件已关闭。');
  } catch (error) {
    console.error('关闭所有文件失败:', error);
  }
}

closeAllFilesExample();
```
