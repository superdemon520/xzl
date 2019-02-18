--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.0
-- Dumped by pg_dump version 9.6.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;


SET search_path = s_message, pg_catalog;

--
-- Name: update_timestamp_when_created(); Type: FUNCTION; Schema: s_message; Owner: postgres
--

CREATE FUNCTION update_timestamp_when_created() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
  declare
      t timestamptz;
  BEGIN
      t := now();
      NEW.created_at = t;
      NEW.updated_at = t;
      RETURN NEW;
  END;
  $$;


ALTER FUNCTION s_message.update_timestamp_when_created() OWNER TO postgres;

--
-- Name: update_timestamp_when_updated(); Type: FUNCTION; Schema: s_message; Owner: postgres
--

CREATE FUNCTION update_timestamp_when_updated() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
  BEGIN
      NEW.updated_at = now();
      RETURN NEW;
  END;
  $$;


ALTER FUNCTION s_message.update_timestamp_when_updated() OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;



--
-- PostgreSQL database dump complete
--

