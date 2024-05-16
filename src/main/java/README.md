# Solving the Git Challenge

Note that this is not a repository showcasing a proper architecture fit for production use. Just use
it as inspiration when trying to solve the Git challenge yourself.

## High-Level Model

```mermaid
---
title: CodeCrafters Git Challenge
---
classDiagram
    GitObject <|-- GitBlob
    GitObject <|-- GitTree
    GitTree *-- GitTreeEntry
    GitTreeEntry -- GitTreeEntryMode
    GitObject <|-- GitCommit
    GitObject .. GitObjectFactory
    GitObject .. GitObjectRepository
    GitPack *-- GitPackObject
    GitPackObject *-- DeltaInstruction
    DeltaInstruction -- DeltaType
    GitPack .. GitRemoteRepository
    GitReference .. GitRemoteRepository
    class GitObject {
        +type
        +size
        +content
    }
    class GitBlob {
    }
    class GitTree {
        +entries
    }
    class GitTreeEntry {
        +mode
        +name
        +hash
    }
    class GitTreeEntryMode {
        <<enumeration>>
        REGULAR
        EXECUTABLE
        SYMLINK
        DIRECTORY
    }
    class GitCommit {
        +threeHash
        +parentCommits
        +authors
        +committers
        +message
    }
    class GitObjectFactory {
        +createGitObject(rawBytes) GitObject
    }
    class GitObjectRepository {
        +read(hash) GitObject
        +write(object) String
    }
    class GitRemoteRepository {
        +getReferences(repoUrl) GitReferences
        +getPack(repoUrl, references) GitPack
    }
    class GitPack {
        +packVersion
        +packObjects
    }
    class GitPackObject {
        +type
        +size
        +content
        +isDeltified
        +deltaInstructions
    }
    class DeltaInstruction {
        +type DeltaType
        +data
        +offset
        +size
    }
    class DeltaType {
        <<enumeration>>
        COPY
        INSERT
    }
```