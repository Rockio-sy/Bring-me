ALTER TABLE public.requests
ALTER COLUMN removed_at DROP DEFAULT,
ALTER COLUMN removed_at SET DEFAULT NULL;

ALTER TABLE public.requests
ADD CONSTRAINT check_positive_price CHECK (price >= 0);

