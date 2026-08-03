import { Box, Button, Stack, Typography } from '@mui/material'
import { useRef, useState, type ChangeEvent } from 'react'

type ImageUploadFieldProps = {
  label: string
  imageUrl?: string | null
  uploading?: boolean
  onSelect: (file: File) => void
  onClear?: () => void
}

export function ImageUploadField({
  label,
  imageUrl,
  uploading = false,
  onSelect,
  onClear,
}: ImageUploadFieldProps) {
  const inputRef = useRef<HTMLInputElement>(null)
  const [preview, setPreview] = useState<string | null>(null)

  const shownUrl = preview ?? imageUrl ?? null

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0]
    if (!file) return
    setPreview(URL.createObjectURL(file))
    onSelect(file)
    event.target.value = ''
  }

  return (
    <Stack spacing={1.5}>
      <Typography variant="subtitle1">{label}</Typography>
      <Box
        sx={{
          height: 160,
          borderRadius: 1,
          border: 1,
          borderColor: 'divider',
          overflow: 'hidden',
          background: shownUrl
            ? undefined
            : 'linear-gradient(135deg, #FFE8EA 0%, #FFD0D4 50%, #F7F7F5 100%)',
          backgroundImage: shownUrl ? `url(${shownUrl})` : undefined,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
        }}
      />
      <Stack direction="row" spacing={1}>
        <Button
          variant="outlined"
          disabled={uploading}
          onClick={() => inputRef.current?.click()}
        >
          {uploading ? 'Enviando…' : 'Escolher imagem'}
        </Button>
        {onClear && (imageUrl || preview) ? (
          <Button
            color="inherit"
            disabled={uploading}
            onClick={() => {
              setPreview(null)
              onClear()
            }}
          >
            Remover
          </Button>
        ) : null}
      </Stack>
      <input
        ref={inputRef}
        type="file"
        accept="image/jpeg,image/png,image/webp,image/gif"
        hidden
        onChange={handleChange}
      />
    </Stack>
  )
}
