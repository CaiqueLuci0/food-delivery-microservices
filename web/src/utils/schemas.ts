import { z } from 'zod'

export const addressSchema = z.object({
  cep: z.string().min(8, 'CEP inválido'),
  logradouro: z.string().min(1, 'Obrigatório'),
  numero: z.string().min(1, 'Obrigatório'),
  complemento: z.string().optional().nullable(),
  bairro: z.string().min(1, 'Obrigatório'),
  cidade: z.string().min(1, 'Obrigatório'),
  uf: z.string().min(2, 'UF inválida').max(2),
  referencia: z.string().optional().nullable(),
})

export const loginSchema = z.object({
  email: z.string().email('E-mail inválido'),
  password: z.string().min(1, 'Informe a senha'),
})

export const registerSchema = z.object({
  name: z.string().min(2, 'Nome muito curto'),
  email: z.string().email('E-mail inválido'),
  password: z.string().min(6, 'Mínimo 6 caracteres'),
  address: addressSchema,
})

export const restaurantSchema = z.object({
  name: z.string().min(2, 'Nome obrigatório'),
  description: z.string().min(2, 'Descrição obrigatória'),
  address: addressSchema,
})

export const specOptionSchema = z.object({
  name: z.string().min(1, 'Nome da opção obrigatório'),
  description: z.string().optional().nullable(),
  extraPrice: z.coerce.number().min(0, 'Preço extra inválido'),
})

export const specificationSchema = z.object({
  name: z.string().min(1, 'Nome da especificação obrigatório'),
  description: z.string().optional().nullable(),
  specOptions: z.array(specOptionSchema),
})

export const productSchema = z.object({
  name: z.string().min(2, 'Nome obrigatório'),
  price: z.coerce.number().positive('Preço inválido'),
  description: z.string().optional().nullable(),
  specifications: z.array(specificationSchema),
})
