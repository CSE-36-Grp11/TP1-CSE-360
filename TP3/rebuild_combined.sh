#!/bin/sh
set -e

out="TP3/TP3javadoc_Combined.html"
{
  printf '%s\n' '<!DOCTYPE html>'
  printf '%s\n' '<html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">'
  printf '%s\n' '<title>TP3javadoc Combined Javadoc</title>'
  printf '%s\n' '<style>body{font-family:Arial,sans-serif;margin:20px;line-height:1.45}h1{margin-bottom:6px}h2{margin-top:26px;padding-top:10px;border-top:1px solid #ddd}hr{border:none;border-top:1px solid #ccc;margin:24px 0}.section{margin:14px 0}a{pointer-events:none;color:inherit;text-decoration:none}</style>'
  printf '%s\n' '</head><body><h1>TP3javadoc Combined Javadoc</h1><p>Single-page combined content with links disabled for easy PDF export.</p>'
} > "$out"
find "TP3/New Code Javadoc" -type f -name '*.html' | sort | while IFS= read -r f; do
  {
    printf '%s\n' '<hr>'
    printf '%s\n' "<h2>$f</h2><div class=\"section\">"
    sed -n '/<body[^>]*>/,/<\/body>/p' "$f" | sed '1d;$d' | sed -E 's/<a [^>]*>//g; s#</a>##g'
    printf '%s\n' '</div>'
  } >> "$out"
done
printf '%s\n' '</body></html>' >> "$out"

out="TP3/TP3Test_javadoc_Combined.html"
{
  printf '%s\n' '<!DOCTYPE html>'
  printf '%s\n' '<html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">'
  printf '%s\n' '<title>TP3Test javadoc Combined</title>'
  printf '%s\n' '<style>body{font-family:Arial,sans-serif;margin:20px;line-height:1.45}h1{margin-bottom:6px}h2{margin-top:26px;padding-top:10px;border-top:1px solid #ddd}hr{border:none;border-top:1px solid #ccc;margin:24px 0}.section{margin:14px 0}a{pointer-events:none;color:inherit;text-decoration:none}</style>'
  printf '%s\n' '</head><body><h1>TP3Test javadoc Combined</h1><p>Single-page combined content with links disabled for easy PDF export.</p>'
} > "$out"
find "TP3/Test Code Javadoc" -type f -name '*.html' | sort | while IFS= read -r f; do
  {
    printf '%s\n' '<hr>'
    printf '%s\n' "<h2>$f</h2><div class=\"section\">"
    sed -n '/<body[^>]*>/,/<\/body>/p' "$f" | sed '1d;$d' | sed -E 's/<a [^>]*>//g; s#</a>##g'
    printf '%s\n' '</div>'
  } >> "$out"
done
printf '%s\n' '</body></html>' >> "$out"
