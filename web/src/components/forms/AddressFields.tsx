import { Stack, TextField } from '@mui/material'
import type { Control, FieldPath, FieldValues } from 'react-hook-form'
import { Controller } from 'react-hook-form'
import { formatAddressNumber, formatCep, formatUf } from '@/utils/masks'

type AddressFieldsProps<T extends FieldValues> = {
  control: Control<T>
  prefix: FieldPath<T>
}

export function AddressFields<T extends FieldValues>({ control, prefix }: AddressFieldsProps<T>) {
  const field = (name: string) => `${String(prefix)}.${name}` as FieldPath<T>

  return (
    <Stack spacing={2}>
      <Controller
        name={field('cep')}
        control={control}
        render={({ field: f, fieldState }) => (
          <TextField
            {...f}
            value={formatCep(String(f.value ?? ''))}
            onChange={(event) => f.onChange(formatCep(event.target.value))}
            label="CEP"
            placeholder="00000-000"
            inputProps={{ inputMode: 'numeric', maxLength: 9 }}
            error={Boolean(fieldState.error)}
            helperText={fieldState.error?.message}
            fullWidth
          />
        )}
      />
      <Controller
        name={field('logradouro')}
        control={control}
        render={({ field: f, fieldState }) => (
          <TextField
            {...f}
            label="Logradouro"
            error={Boolean(fieldState.error)}
            helperText={fieldState.error?.message}
            fullWidth
          />
        )}
      />
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
        <Controller
          name={field('numero')}
          control={control}
          render={({ field: f, fieldState }) => (
            <TextField
              {...f}
              value={formatAddressNumber(String(f.value ?? ''))}
              onChange={(event) => f.onChange(formatAddressNumber(event.target.value))}
              label="Número"
              placeholder="123 ou S/N"
              inputProps={{ maxLength: 12 }}
              error={Boolean(fieldState.error)}
              helperText={fieldState.error?.message}
              fullWidth
            />
          )}
        />
        <Controller
          name={field('complemento')}
          control={control}
          render={({ field: f }) => <TextField {...f} label="Complemento" fullWidth />}
        />
      </Stack>
      <Controller
        name={field('bairro')}
        control={control}
        render={({ field: f, fieldState }) => (
          <TextField
            {...f}
            label="Bairro"
            error={Boolean(fieldState.error)}
            helperText={fieldState.error?.message}
            fullWidth
          />
        )}
      />
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
        <Controller
          name={field('cidade')}
          control={control}
          render={({ field: f, fieldState }) => (
            <TextField
              {...f}
              label="Cidade"
              error={Boolean(fieldState.error)}
              helperText={fieldState.error?.message}
              fullWidth
            />
          )}
        />
        <Controller
          name={field('uf')}
          control={control}
          render={({ field: f, fieldState }) => (
            <TextField
              {...f}
              value={formatUf(String(f.value ?? ''))}
              onChange={(event) => f.onChange(formatUf(event.target.value))}
              label="UF"
              placeholder="SP"
              inputProps={{ maxLength: 2, style: { textTransform: 'uppercase' } }}
              error={Boolean(fieldState.error)}
              helperText={fieldState.error?.message}
              sx={{ maxWidth: { sm: 120 } }}
              fullWidth
            />
          )}
        />
      </Stack>
      <Controller
        name={field('referencia')}
        control={control}
        render={({ field: f }) => <TextField {...f} label="Referência" fullWidth />}
      />
    </Stack>
  )
}
