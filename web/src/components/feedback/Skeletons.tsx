import { Card, CardContent, Skeleton, Stack } from '@mui/material'

export function RestaurantCardSkeleton() {
  return (
    <Card>
      <CardContent>
        <Stack spacing={1}>
          <Skeleton variant="rounded" height={120} />
          <Skeleton width="60%" />
          <Skeleton width="90%" />
        </Stack>
      </CardContent>
    </Card>
  )
}

export function ListSkeleton({ count = 6 }: { count?: number }) {
  return (
    <Stack spacing={2}>
      {Array.from({ length: count }).map((_, index) => (
        <RestaurantCardSkeleton key={index} />
      ))}
    </Stack>
  )
}
