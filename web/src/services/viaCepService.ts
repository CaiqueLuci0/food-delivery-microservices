import { cepDigits } from '@/utils/masks'

export type ViaCepAddress = {
  cep: string
  logradouro: string
  bairro: string
  cidade: string
  uf: string
}

type ViaCepApiResponse = {
  cep?: string
  logradouro?: string
  bairro?: string
  localidade?: string
  uf?: string
  erro?: boolean
}

export async function lookupCep(cep: string): Promise<ViaCepAddress | null> {
  const digits = cepDigits(cep)
  if (digits.length !== 8) {
    return null
  }

  const response = await fetch(`https://viacep.com.br/ws/${digits}/json/`)
  if (!response.ok) {
    throw new Error('Não foi possível consultar o CEP')
  }

  const data = (await response.json()) as ViaCepApiResponse
  if (data.erro) {
    return null
  }

  return {
    cep: data.cep ?? digits,
    logradouro: data.logradouro ?? '',
    bairro: data.bairro ?? '',
    cidade: data.localidade ?? '',
    uf: data.uf ?? '',
  }
}
