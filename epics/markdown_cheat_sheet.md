
## Links

[Visit GitHub!](www.github.com)

## Nested lists

1. Item 1
  1. A corollary to the above item.
  2. Yet another point to consider.
2. Item 2
  * A corollary that does not need to be ordered.
    * This is indented four spaces, because it's two spaces further than the item above.
    * You might want to consider making a new list.
3. Item 3

## Task lists

- [x] @mentions, #refs, [links](), **formatting**, and <del>tags</del> are supported
- [x] list syntax is required (any unordered or ordered list supported)
- [x] this is a complete item
- [ ] this is an incomplete item


## Strikethrough

~~Mistaken text.~~

## Fenced code blocks

```
function test() {
  console.log("notice the blank line before this function?");
}
```

## Syntax highlighting

```ruby
require 'redcarpet'
markdown = Redcarpet.new("Hello World!")
puts markdown.to_html
```

## Tables

| Left-Aligned  | Center Aligned  | Right Aligned |
| :------------ |:---------------:| -----:|
| col 3 is      | some wordy text | $1600 |
| col 2 is      | centered        |   $12 |
| zebra stripes | are neat        |    $1 |

# References

Certain references are auto-linked:

* SHA: 0d24e0fa28f1c662bab65e5eb909c23e9cf18b5d
* User@SHA: alexfdz@0d24e0fa28f1c662bab65e5eb909c23e9cf18b5d
* User/Repository@SHA: alexfdz/hoozad.js@0d24e0fa28f1c662bab65e5eb909c23e9cf18b5d
* #Num: #1
* GH-Num: GH-1
* User#Num: alexfdz#1
* User/Repository#Num: alexfdz/hoozad.js#1