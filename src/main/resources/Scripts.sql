-- 1.Which user has duplicated installs (more than one install) for yesterday?
-- Assumes that the quality of the data is ok, so data cleaning was already done and no repeated data is present.
-- Uses postgresql. Besides the id provides the amount of install in the requested time frame.

select user_id, count(*) install_counts from user_first_install_fact where installed_at between TIMESTAMP 'yesterday' and TIMESTAMP 'today' group by user_id having count(*) > 1

-- 2. Which are the top 5 channels with the most installs coming in from Android?
-- Query done with the information provided. Based on usage/data scenarios i think that some of the required data could
-- be 'previously calculated'
select * from channel_dim where channel_sk in(
--This section get the top N (and because is only 5 the in shouldn't be a problem)
select channel_sk from (
-- Internal stats filtered by the OS (used to later get the top N)
select uf.channel_sk, count(*) install_count  from user_first_install_fact uf,  client_dim cd
where uf.client_sk = cd.client_sk  and cd.os_name = 'Android'
group by channel_sk order by 2 desc
) as internal_stats_table limit 5)
