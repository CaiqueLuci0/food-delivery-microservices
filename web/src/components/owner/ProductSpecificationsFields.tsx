import { Box, Button, Divider, Stack, TextField, Typography } from '@mui/material'
import { Controller, useFieldArray, type Control } from 'react-hook-form'
import type { z } from 'zod'
import type { productSchema } from '@/utils/schemas'

type ProductFormValues = z.infer<typeof productSchema>

type ProductSpecificationsFieldsProps = {
  control: Control<ProductFormValues>
}

function SpecOptionsFields({
  control,
  specIndex,
}: {
  control: Control<ProductFormValues>
  specIndex: number
}) {
  const { fields, append, remove } = useFieldArray({
    control,
    name: `specifications.${specIndex}.specOptions`,
  })

  return (
    <Stack spacing={1.5} pl={1} borderLeft={2} borderColor="divider">
      <Typography variant="subtitle2" color="text.secondary">
        Opções
      </Typography>
      {fields.map((field, optionIndex) => (
        <Stack key={field.id} spacing={1.5}>
          {optionIndex > 0 ? <Divider /> : null}
          <Controller
            name={`specifications.${specIndex}.specOptions.${optionIndex}.name`}
            control={control}
            render={({ field: f, fieldState }) => (
              <TextField
                {...f}
                label="Nome da opção"
                size="small"
                error={Boolean(fieldState.error)}
                helperText={fieldState.error?.message}
                fullWidth
              />
            )}
          />
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5}>
            <Controller
              name={`specifications.${specIndex}.specOptions.${optionIndex}.description`}
              control={control}
              render={({ field: f }) => (
                <TextField
                  {...f}
                  value={f.value ?? ''}
                  label="Descrição"
                  size="small"
                  fullWidth
                />
              )}
            />
            <Controller
              name={`specifications.${specIndex}.specOptions.${optionIndex}.extraPrice`}
              control={control}
              render={({ field: f, fieldState }) => (
                <TextField
                  {...f}
                  label="Preço extra"
                  type="number"
                  size="small"
                  error={Boolean(fieldState.error)}
                  helperText={fieldState.error?.message}
                  sx={{ minWidth: { sm: 140 } }}
                />
              )}
            />
          </Stack>
          <Box>
            <Button color="error" size="small" onClick={() => remove(optionIndex)}>
              Remover opção
            </Button>
          </Box>
        </Stack>
      ))}
      <Box>
        <Button
          size="small"
          onClick={() => append({ name: '', description: '', extraPrice: 0 })}
        >
          Adicionar opção
        </Button>
      </Box>
    </Stack>
  )
}

export function ProductSpecificationsFields({ control }: ProductSpecificationsFieldsProps) {
  const { fields, append, remove } = useFieldArray({
    control,
    name: 'specifications',
  })

  return (
    <Stack spacing={2}>
      <Typography variant="h6">Especificações</Typography>
      {fields.length === 0 ? (
        <Typography variant="body2" color="text.secondary">
          Nenhuma especificação. Adicione tamanho, adicionais, etc.
        </Typography>
      ) : null}
      {fields.map((field, index) => (
        <Box
          key={field.id}
          sx={{
            border: 1,
            borderColor: 'divider',
            borderRadius: 1,
            p: 2,
          }}
        >
          <Stack spacing={2}>
            <Controller
              name={`specifications.${index}.name`}
              control={control}
              render={({ field: f, fieldState }) => (
                <TextField
                  {...f}
                  label="Nome da especificação"
                  error={Boolean(fieldState.error)}
                  helperText={fieldState.error?.message}
                  fullWidth
                />
              )}
            />
            <Controller
              name={`specifications.${index}.description`}
              control={control}
              render={({ field: f }) => (
                <TextField
                  {...f}
                  value={f.value ?? ''}
                  label="Descrição"
                  fullWidth
                  multiline
                />
              )}
            />
            <SpecOptionsFields control={control} specIndex={index} />
            <Box>
              <Button color="error" size="small" onClick={() => remove(index)}>
                Remover especificação
              </Button>
            </Box>
          </Stack>
        </Box>
      ))}
      <Box>
        <Button
          variant="outlined"
          onClick={() =>
            append({
              name: '',
              description: '',
              specOptions: [{ name: '', description: '', extraPrice: 0 }],
            })
          }
        >
          Adicionar especificação
        </Button>
      </Box>
    </Stack>
  )
}
