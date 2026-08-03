import { CircularProgress, InputAdornment, Stack, TextField } from '@mui/material'
import { useEffect, useRef, useState } from 'react'
import type { Control, FieldPath, FieldValues, PathValue, UseFormSetValue } from 'react-hook-form'
import { Controller, useWatch } from 'react-hook-form'
import { lookupCep } from '@/services/viaCepService'
import { cepDigits, formatAddressNumber, formatCep, formatUf } from '@/utils/masks'

type AddressFieldsProps<T extends FieldValues> = {
  control: Control<T>
  setValue: UseFormSetValue<T>
  prefix: FieldPath<T>
}

export function AddressFields<T extends FieldValues>({
  control,
  setValue,
  prefix,
}: AddressFieldsProps<T>) {
  const field = (name: string) => `${String(prefix)}.${name}` as FieldPath<T>
  const cepValue = useWatch({ control, name: field('cep') })
  const [lookingUp, setLookingUp] = useState(false)
  const [cepError, setCepError] = useState<string | null>(null)
  const lastLookupRef = useRef<string | null>(null)

  useEffect(() => {
    const digits = cepDigits(String(cepValue ?? ''))
    if (digits.length !== 8) {
      setCepError(null)
      lastLookupRef.current = null
      return
    }
    if (lastLookupRef.current === digits) {
      return
    }

    let cancelled = false
    lastLookupRef.current = digits
    setLookingUp(true)
    setCepError(null)

    void lookupCep(digits)
      .then((address) => {
        if (cancelled) return
        if (!address) {
          setCepError('CEP não encontrado')
          return
        }
        setValue(field('logradouro'), address.logradouro as PathValue<T, FieldPath<T>>, {
          shouldDirty: true,
          shouldValidate: true,
        })
        setValue(field('bairro'), address.bairro as PathValue<T, FieldPath<T>>, {
          shouldDirty: true,
          shouldValidate: true,
        })
        setValue(field('cidade'), address.cidade as PathValue<T, FieldPath<T>>, {
          shouldDirty: true,
          shouldValidate: true,
        })
        setValue(field('uf'), formatUf(address.uf) as PathValue<T, FieldPath<T>>, {
          shouldDirty: true,
          shouldValidate: true,
        })
      })
      .catch(() => {
        if (!cancelled) {
          setCepError('Não foi possível consultar o CEP')
          lastLookupRef.current = null
        }
      })
      .finally(() => {
        if (!cancelled) setLookingUp(false)
      })

    return () => {
      cancelled = true
    }
  }, [cepValue, prefix, setValue])

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
            error={Boolean(fieldState.error) || Boolean(cepError)}
            helperText={fieldState.error?.message ?? cepError ?? undefined}
            InputProps={{
              endAdornment: lookingUp ? (
                <InputAdornment position="end">
                  <CircularProgress size={18} />
                </InputAdornment>
              ) : undefined,
            }}
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
